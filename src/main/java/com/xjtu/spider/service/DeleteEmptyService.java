package com.xjtu.spider.service;

import com.xjtu.spider.domain.Image;
import com.xjtu.spider.domain.Text;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * 删除内容为空的文本碎片和图片碎片
 * 1. 删除空文本：delEmptyContentSingleTerm
 * 2. 删除空图片：delEmptyContentSingleTermImage
 * <p>
 * Created by yuanhao on 2017/3/16.
 */
@Service
public class DeleteEmptyService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 删除内容为空的碎片
     *
     * @param list 单个term下的碎片集合
     * @return 处理后的碎片集合
     */
    public List<Text> delEmptyContentSingleTerm(List<Text> list) {
        for (int i = 0; i < list.size(); i++) {
            Text fragment = list.get(i);
            if (fragment.getFragmentContent().equals("")
                    || fragment.getFragmentContent().length() == 0
                    || fragment.getFragmentContent().equals(null)) { // 删除内容为空的碎片
                list.remove(i);
            }
        }
        return list;
    }

    /**
     * 删除内容为空的图片
     *
     * @param list 单个term下的图片集合
     * @return 处理后的碎片集合
     */
    public List<Image> delEmptyContentSingleTermImage(List<Image> list) {
        for (int i = 0; i < list.size(); i++) {
            Image fragment = list.get(i);
            byte[] imageContent = fragment.getImageContent();
            try {
                logger.info("图片流大小：" + imageContent.length);
                if (imageContent.length == 0) { // 删除内容为空的碎片
                    logger.info("图片流内容为空，删除该图片...");
                    list.remove(i);
                }
            } catch (Exception e) {
                logger.error("图片流内容为空，删除该图片");
                list.remove(i);
            }

        }

        return list;
    }


}
