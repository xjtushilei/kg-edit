package com.xjtu.visualization.web;

import com.xjtu.common.domain.Error;
import com.xjtu.dashboard.repository.ClassStatusRepository;
import com.xjtu.datainput.domain.Term;
import com.xjtu.datainput.repository.CatalogRepository;
import com.xjtu.datainput.repository.RelationRepository;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.dependency.domain.Dependency;
import com.xjtu.dependency.repository.DependencyRepository;
import com.xjtu.spider.domain.Facet;
import com.xjtu.spider.repository.ErrorTermRepository;
import com.xjtu.spider.repository.FacetRepository;
import com.xjtu.spider.repository.ImageRepository;
import com.xjtu.spider.repository.TextRepository;
import com.xjtu.visualization.domain.Edge;
import com.xjtu.visualization.domain.FacetResult;
import com.xjtu.visualization.domain.Node;
import com.xjtu.visualization.utils.VisualUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 爬虫的API
 * Created by yuanhao on 2017/3/16.
 */

@RestController
@RequestMapping("/visualization")
public class VisualizationController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    // 数据库相关操作实例
    @Autowired
    private TextRepository textRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private TermRepository termRepository;
    @Autowired
    private ErrorTermRepository errorTermRepository;
    @Autowired
    private ClassStatusRepository classStatusRepository;
    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private FacetRepository facetRepository;

    @Autowired
    private DependencyRepository dependencyRepository;


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "获取知识点总图", notes = "输入课程名，获取所有知识点")
    public ResponseEntity getall(
            @RequestParam(value = "ClassID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String classID
    ) {
        FacetResult result = new FacetResult();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        try {
            List<Dependency> dependencyList = dependencyRepository.findByClassID(classID);
            for (Dependency d : dependencyList) {
                edges.add(new Edge(d.getStartTermID().toString(), null, d.getEndTermID().toString(), 1));
            }

            List<Term> termList = termRepository.findByClassID(classID);
            for (Term term : termList) {
                int size = VisualUtils.getSize(dependencyList, term.getTermID());
                String color = VisualUtils.getcolor("随意");
                nodes.add(new Node(term.getTermName(), null, term.getTermID().toString(), size, color));
            }

            result.setEdges(edges);
            result.setNodes(nodes);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            logger.error("获取课程下的知识点失败.classID=" + classID, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("课程ID为：" + classID + "的课程获取失败"));
        }

    }

    @RequestMapping(value = "/getDependency", method = RequestMethod.GET)
    @ApiOperation(value = "获取知识点连接图", notes = "输入id，获取相连的知识点")
    public ResponseEntity getDependency(
            @RequestParam(value = "TermID", defaultValue = "1") Long termID,
            @RequestParam(value = "TermName", defaultValue = "1") String termName
    ) {
        FacetResult result = new FacetResult();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        try {
            List<Dependency> dependencyList = dependencyRepository.findByStartTermIDOrEndTermID(termID, termID);
            for (Dependency d : dependencyList) {
                edges.add(new Edge(d.getStartTermID().toString(), null, d.getEndTermID().toString(), 1));
                if (d.getStartTermID() == termID) {
                    nodes.add(new Node(d.getEndTermName(), null, d.getEndTermID().toString(), 1, VisualUtils.getcolor("")));
                } else if (d.getEndTermID() == termID) {
                    nodes.add(new Node(d.getStartTermName(), null, d.getStartTermID().toString(), 1, VisualUtils.getcolor("")));

                }
            }
            nodes.add(new Node(termName, null, termID.toString(), dependencyList.size(), VisualUtils.getcolor("中心")));


            result.setEdges(edges);
            result.setNodes(nodes);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            logger.error("获取知识点失败.termID=" + termID, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("termID为：" + termID + "的知识点获取失败"));
        }

    }

    @RequestMapping(value = "/getfacet", method = RequestMethod.GET)
    @ApiOperation(value = "获取分面信息", notes = "输入termid，获取拥有的分面")
    public ResponseEntity getfacet(
            @RequestParam(value = "TermID", defaultValue = "1") Long termID,
            @RequestParam(value = "TermName", defaultValue = "1") String termName
    ) {
        FacetResult result = new FacetResult();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();

        try {
            List<Facet> facetList = facetRepository.findByTermID(termID);
            Set<String> facetSet = new HashSet<>();
            for (Facet facet : facetList) {
                facetSet.add(facet.getFacetName());
            }

            //装分面
            for (String facet : facetSet) {
                // System.out.println(facet);
                nodes.add(new Node(facet, null, facet, 1, VisualUtils.getcolor("随机")));
                edges.add(new Edge(termID.toString(), null, facet, 1));
            }
            nodes.add(new Node(termName, null, termID.toString(), facetSet.size(), VisualUtils.getcolor("中心")));// 加入本节点

            result.setEdges(edges);
            result.setNodes(nodes);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            logger.error("获取分面信息失败.termID=" + termID, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("termID为：" + termID + "的分面信息获取失败"));
        }

    }

    @RequestMapping(value = "/getdetailed", method = RequestMethod.GET)
    @ApiOperation(value = "获取某一个分的具体信息", notes = "输入知识图谱系统的知识点id，以及该分面（分面的意思为属性）的分面id，获取某一个分面的具体信息")
    public ResponseEntity getdetailed(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "FacetName", defaultValue = "摘要") String FacetName
    ) {
        Map<String, Object> result = new HashMap<>();
        List<String> textList = new LinkedList<>();
        List<String> imageList = new LinkedList<>();
        try {
            imageList = imageRepository.findImageUrl(TermID, FacetName);
            textRepository.findByTermIDAndFacetName(TermID, FacetName).iterator().forEachRemaining(id -> {
                textList.add(id.getFragmentContent());
            });
            result.put("text", textList);
            result.put("photo", imageList);
        } catch (Exception e) {
            logger.error("查找知识点某分面的碎片 失败。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("查找知识点某分面的碎片 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @RequestMapping(value = "/getKnowledgesByChapterId", method = RequestMethod.GET)
    @ApiOperation(value = "获取知识点连接图", notes = "输入id，获取相连的知识点")
    public ResponseEntity getKnowledgesByChapterId(
            @RequestParam(value = "ParentChapterID", defaultValue = "1") String ParentChapterID,
            @RequestParam(value = "ChapterID", defaultValue = "1") String ChapterID,
            @RequestParam(value = "ChildrenChapterID", defaultValue = "1") String ChildrenChapterID
    ) {
        FacetResult result = new FacetResult();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        try {
            Set<Dependency> dependencySet = new HashSet<>();
            Set<Long> dependencyIdSet = new HashSet<>();
            List<Term> termList = termRepository.findByCatalog(ParentChapterID, ChapterID, ChildrenChapterID);
            for (Term term : termList) {
                dependencyIdSet.add(term.getTermID());
                List<Dependency> dependencyList = dependencyRepository.findByStartTermIDOrEndTermID(term.getTermID(), term.getTermID());
                logger.debug(dependencyList.size() + " dependencyList");
                for (Dependency dependency : dependencyList) {
                    dependencySet.add(dependency);
                }
                logger.debug(dependencySet.size() + " dependencySet");
                nodes.add(new Node(term.getTermName(), null, term.getTermID().toString(), dependencyList.size(), VisualUtils.getcolor("中心")));
            }

            for (Dependency d : dependencySet) {
                edges.add(new Edge(d.getStartTermID().toString(), null, d.getEndTermID().toString(), 1));
                if (dependencyIdSet.contains(d.getStartTermID())) {
                    nodes.add(new Node(d.getEndTermName(), null, d.getEndTermID().toString(), 1, VisualUtils.getcolor("")));
                } else if (dependencyIdSet.contains(d.getEndTermID())) {
                    nodes.add(new Node(d.getStartTermName(), null, d.getStartTermID().toString(), 1, VisualUtils.getcolor("")));
                }
            }


            result.setEdges(edges);
            result.setNodes(nodes);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            logger.error("获取知识点失败", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("知识点获取失败"));
        }

    }

}
