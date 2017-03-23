package com.xjtu.rectification.web;

import com.xjtu.common.Config;
import com.xjtu.common.domain.Error;
import com.xjtu.common.domain.Success;
import com.xjtu.datainput.domain.Term;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.spider.domain.Facet;
import com.xjtu.spider.domain.Image;
import com.xjtu.spider.domain.Text;
import com.xjtu.spider.repository.FacetRepository;
import com.xjtu.spider.repository.ImageRepository;
import com.xjtu.spider.repository.TextRepository;
import com.xjtu.spider.service.ParseService;
import com.xjtu.spider.web.SpiderController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created by shilei on 2017/3/13.
 */

@RestController
@RequestMapping("/rectification")
public class RectificationController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ParseService parseService;

    @Autowired
    private TermRepository termRepository;
    @Autowired
    private FacetRepository facetRepository;
    @Autowired
    private TextRepository textRepository;
    @Autowired
    private ImageRepository imageRepository;

    @RequestMapping(value = "/getClassTerm", method = RequestMethod.GET)
    public ResponseEntity getClassTerm(
            @RequestParam(value = "ClassID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String ClassID
    ) {
        List<Term> result = new LinkedList<>();
        try {
            result = termRepository.findByClassID(ClassID);
        } catch (Exception e) {
            logger.error("查找所有知识点 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("查找所有知识点 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getTermFacet", method = RequestMethod.GET)
    public ResponseEntity getTermFacet(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID
    ) {
        Set<String> facetSet = new HashSet<>();
        List<Facet> result = new LinkedList<>();
        try {
            result = facetRepository.findByTermID(TermID);
            for (Facet facet : result) {
                facetSet.add(facet.getFacetName());
            }
        } catch (Exception e) {
            logger.error("查找知识点的分面 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("查找知识点的分面 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(facetSet);
    }

    @RequestMapping(value = "/getFacetFragment", method = RequestMethod.GET)
    public ResponseEntity getFacetFragment(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "FacetName", defaultValue = "摘要") String FacetName
    ) {
        Map<String, Object> result = new HashMap<>();
        List<Text> textList = new LinkedList<>();
        List<Image> imageList = new LinkedList<>();
        try {
            textList = textRepository.findByTermIDAndFacetName(TermID, FacetName);
            imageList = imageRepository.findByTermIDAndFacetName(TermID, FacetName);
            result.put("text", textList);
            result.put("image", imageList);
        } catch (Exception e) {
            logger.error("查找知识点某分面的碎片 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("查找知识点某分面的碎片 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/deleteImage", method = RequestMethod.GET)
    public ResponseEntity deleteImage(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "ImageID", defaultValue = "43") Long ImageID
    ) {
        try {
            imageRepository.deleteByTermIDAndImageID(TermID, ImageID);
        } catch (Exception e) {
            logger.error("删除知识点某分面的图片 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("删除知识点某分面的图片 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Success("删除知识点某分面的图片 成功！"));
    }

    @RequestMapping(value = "/deleteText", method = RequestMethod.GET)
    public ResponseEntity deleteText(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "FragmentID", defaultValue = "43") Long FragmentID
    ) {
        try {
            textRepository.deleteByTermIDAndFragmentID(TermID, FragmentID);
        } catch (Exception e) {
            logger.error("删除知识点某分面的文本 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("删除知识点某分面的文本 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Success("删除知识点某分面的文本 成功！"));
    }

    @RequestMapping(value = "/deleteFacet", method = RequestMethod.GET)
    public ResponseEntity deleteFacet(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "FacetName", defaultValue = "摘要") String FacetName
    ) {
        try {
            textRepository.deleteByTermIDAndFacetName(TermID, FacetName);
            imageRepository.deleteByTermIDAndFacetName(TermID, FacetName);
            facetRepository.deleteByTermIDAndFacetName(TermID, FacetName);
        } catch (Exception e) {
            logger.error("删除知识点某分面 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("删除知识点某分面 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Success("删除知识点某分面 成功！"));
    }

    @RequestMapping(value = "/updateText", method = RequestMethod.POST)
    public ResponseEntity updateText(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "FragmentID", defaultValue = "43") Long FragmentID,
            @RequestParam(value = "FragmentContent", defaultValue = "更新之后的文本") String FragmentContent
    ) {
        try {
            textRepository.updateByTermIDAndFragmentIDAndFragmentContent(TermID, FragmentID, FragmentContent);
        } catch (Exception e) {
            logger.error("更新知识点某分面的文本 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("更新知识点某分面的文本 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Success("更新知识点某分面的文本 成功！"));
    }

    @RequestMapping(value = "/updateFacet", method = RequestMethod.GET)
    public ResponseEntity updateFacet(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "OldFacetName", defaultValue = "摘要") String OldFacetName,
            @RequestParam(value = "NewFacetName", defaultValue = "更新后的分面") String NewFacetName
    ) {
        try {
            textRepository.updateByTermIDAndFacetName(TermID, OldFacetName, NewFacetName);
            imageRepository.updateByTermIDAndFacetName(TermID, OldFacetName, NewFacetName);
            facetRepository.updateByTermIDAndFacetName(TermID, OldFacetName, NewFacetName);
        } catch (Exception e) {
            logger.error("更新知识点某分面 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("更新知识点某分面 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Success("更新知识点某分面 成功！"));
    }

    @RequestMapping(value = "/addText", method = RequestMethod.POST)
    public ResponseEntity addText(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "FacetName", defaultValue = "摘要") String FacetName,
            @RequestParam(value = "FragmentContent", defaultValue = "添加之后的文本") String FragmentContent,
            @RequestParam(value = "TermName", defaultValue = "TermName") String TermName
    ) {

        Text text = new Text();
        try {
            text.setFragmentContent(FragmentContent);
            text.setFragmentUrl("null");
            text.setFragmentPostTime(parseService.getScratchTime());
            text.setFragmentScratchTime(parseService.getScratchTime());
            text.setTermID(TermID);
            text.setTermName(TermName);
            text.setFacetName(FacetName);
            textRepository.save(text);
        } catch (Exception e) {
            logger.error("添加知识点某分面的文本 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("添加知识点某分面的文本 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Success("添加知识点某分面的文本 成功！"));
    }

    @RequestMapping(value = "/addFacet", method = RequestMethod.GET)
    public ResponseEntity addFacet(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "FacetName", defaultValue = "添加的分面") String FacetName
    ) {

        Facet facet = new Facet();
        try {
            facet.setTermID(TermID);
            facet.setTermName(facetRepository.findByTermID(TermID).get(0).getTermName());
            facet.setFacetName(FacetName);
            facet.setNote("");
            facetRepository.save(facet);
        } catch (Exception e) {
            logger.error("添加知识点某分面 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("添加知识点某分面 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Success("添加知识点某分面 成功！"));
    }

    @RequestMapping(value = "/addImage", method = RequestMethod.POST)
    public ResponseEntity addImage(
            @RequestParam(value = "TermID", defaultValue = "23") Long TermID,
            @RequestParam(value = "FacetName", defaultValue = "摘要") String FacetName,
            @RequestParam(value = "TermName", defaultValue = "TermName") String TermName,
            @RequestParam("file") MultipartFile file
    ) {

        Image image = new Image();
        try {
            image.setImageContent(file.getBytes());
            image.setImageUrl("http://image.baidu.com/" + file.getOriginalFilename());
            image.setImageScratchTime(parseService.getScratchTime());
            image.setTermID(TermID);
            image.setTermName(TermName);
            image.setFacetName(FacetName);
            image.setImageAPI("");
            imageRepository.save(image);
        } catch (Exception e) {
            logger.error("添加知识点某分面的图片 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("添加知识点某分面的图片 失败！"));
        }
        // 更新图片API
        getImageAllApi();
        return ResponseEntity.status(HttpStatus.OK).body(new Success("添加知识点某分面的图片 成功！"));
    }

    // 更新图片的API
    public void getImageAllApi() {
        ResponseEntity responseEntity = null;
        List<Image> imageList = new ArrayList<>();
        try {
            // 得到所有图片
            imageList = imageRepository.findAll();

            // 更新图片的API字段
            for (int i = 0; i < imageList.size(); i++) {
                Long imageID = imageList.get(i).getImageID();
                String api = "http://" + Config.server + "/" + Config.project + "/" + Config.imageAPICata + "/getImage?imageID=" + imageID;
                imageRepository.updateByImageID(api, imageID); // 根据imageID更新imageAPI
            }

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(imageList);
        } catch (Exception e) {
            logger.error("出现错误：" + e);
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("处理图片API操作失败。。。"));
        }
    }


}
