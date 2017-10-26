package com.zbeninfo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbeninfo.model.Node;
import com.zbeninfo.model.Node.PropsBean.LanBean;
import com.zbeninfo.model.Node.PropsBean.RuleBean;
import com.zbeninfo.model.Path;
import com.zbeninfo.model.Path.DotsBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author: Jack
 * @date: 2017/5/23
 */
public class XmlUtil {


    public static void saveXMLfromFlow(String flow, String location) throws IOException {
        JSONObject flowJson = JSON.parseObject(flow);
        JSONObject statesJson = flowJson.getJSONObject("states");
        JSONObject pathsJson = flowJson.getJSONObject("paths");
        List<Node> nodeList = new ArrayList<>();
        List<Path> pathList = new ArrayList<>();

        Set<String> stateKeySet = statesJson.keySet();
        if (stateKeySet != null && stateKeySet.size() > 0) {
            for (String stateKey : stateKeySet) {
                Node node = JSONObject.toJavaObject(statesJson.getJSONObject(stateKey), Node.class);
                nodeList.add(node);
            }
        }

        Set<String> pathKeySet = pathsJson.keySet();
        if (pathKeySet != null && pathKeySet.size() > 0) {
            for (String pathKey : pathKeySet) {
                Path path = JSONObject.toJavaObject(pathsJson.getJSONObject(pathKey), Path.class);
                pathList.add(path);
            }
        }

        // 创建根节点
        Element root = DocumentHelper.createElement("Flow");
        Document document = DocumentHelper.createDocument(root);

        for (Node node : nodeList) {
            String type = node.getType();
            switch (type) {
                case "flowstart":
                    createFlow(root, node);
                    break;
                case "play":
                    createPlay(root, node);
                    break;
                case "service":
                    createService(root, node);
                    break;
                case "judge":
                    createJudge(root, node);
                    break;
                case "touchkey":
                    createTouchKey(root, node);
                    break;
                case "autooper":
                    createAutooper(root, node);
                    break;
                case "handup":
                    createHandup(root, node);
                    break;
            }
        }

        Element relationsElement = root.addElement("Relations");
        for (Path path : pathList) {
            createRelation(relationsElement, path);
        }

        OutputFormat format = new OutputFormat("    ", true);
        format.setEncoding("UTF-8");// 设置编码格式
        File file = new File(location, "IVR.xml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), format);

        xmlWriter.write(document);
        xmlWriter.close();
    }

    private static void createRelation(Element relationsElement, Path path) {
        Element relationElement = relationsElement.addElement("Relation");
        relationElement.addAttribute("nodeId", path.getFrom());
        relationElement.addAttribute("childNodeId", path.getTo());
        if (path.getDots() != null && path.getDots().size() > 0) {
            Element dotsElement = relationElement.addElement("Dots");
            for (DotsBean dotsBean : path.getDots()) {
                Element dotElement = dotsElement.addElement("Dot");
                dotElement.addElement("x").addText(String.valueOf(dotsBean.getX()));
                dotElement.addElement("y").addText(String.valueOf(dotsBean.getY()));
            }
        }
    }

    private static void createHandup(Element root, Node node) {
        Element handupElement = root.addElement("Handup");
        addCommonAttr(handupElement, node);
        if (node.getProps() != null && node.getProps().getProp() != null) {
            handupElement.addAttribute("reason", node.getProps().getProp().getReason());
        }
    }

    private static void createAutooper(Element root, Node node) {
        Element autooperElement = root.addElement("AutoOper");
        addCommonAttr(autooperElement, node);
        if (node.getProps() != null && node.getProps().getProp() != null) {
            autooperElement.addAttribute("autoOperNum", node.getProps().getProp().getAutoOperNum());
        }
    }

    private static void createTouchKey(Element root, Node node) {
        Element touchKeyElement = root.addElement("TouchKey");
        addCommonAttr(touchKeyElement, node);
        if (node.getProps() != null && node.getProps().getProp() != null) {
            touchKeyElement.addAttribute("timeOut", node.getProps().getProp().getTimeOut());
            touchKeyElement.addAttribute("finishkey", node.getProps().getProp().getFinishkey());
            touchKeyElement.addAttribute("repeatCount", node.getProps().getProp().getRepeatCount());
            touchKeyElement.addAttribute("length", node.getProps().getProp().getLength());
            touchKeyElement.addAttribute("className", node.getProps().getProp().getClassName());
        }
    }

    private static void createJudge(Element root, Node node) {
        Element judgeElement = root.addElement("Judge");
        addCommonAttr(judgeElement, node);
    }

    private static void createService(Element root, Node node) {
        Element serviceElement = root.addElement("Service");
        addCommonAttr(serviceElement, node);
        if (node.getProps() != null && node.getProps().getProp() != null) {
            serviceElement.addAttribute("className", node.getProps().getProp().getClassName());
        }
        createRule(serviceElement, node);
    }

    private static void createPlay(Element root, Node node) {
        Element playElement = root.addElement("Play");
        addCommonAttr(playElement, node);
        if (node.getProps() != null && node.getProps().getProp() != null) {
            playElement.addAttribute("breakFlag", node.getProps().getProp().getBreakFlag());
        }
        createLan(playElement, node);
        createRule(playElement, node);
    }

    private static void addCommonAttr(Element element, Node node) {
        element.addAttribute("id", node.getId());
        element.addAttribute("name", node.getText().getText());
        if (node.getProps() != null && node.getProps().getProp() != null) {
            element.addAttribute("remark", node.getProps().getProp().getRemark());
        }
        createLocation(element, node);
    }

    private static void createFlow(Element root, Node flowNode) {
        root.addAttribute("id", flowNode.getId());
        root.addAttribute("name", flowNode.getText().getText());
        if (flowNode.getProps() != null && flowNode.getProps().getProp() != null) {
            root.addAttribute("neVoicePath", flowNode.getProps().getProp().getNeVoicePath());
            root.addAttribute("enVoicePath", flowNode.getProps().getProp().getEnVoicePath());
        }

        String format = "x:%d,y:%d,width:%d,height:%d";
        String result = String.format(format, flowNode.getAttr().getX(), flowNode.getAttr().getY(), flowNode.getAttr().getWidth(), flowNode.getAttr().getHeight());
        root.addAttribute("location", result);
    }

    private static void createLocation(Element element, Node node) {
        String format = "x:%d,y:%d,width:%d,height:%d";
        String result = String.format(format, node.getAttr().getX(), node.getAttr().getY(), node.getAttr().getWidth(), node.getAttr().getHeight());
        Element locationElement = element.addElement("Location");
        locationElement.addText(result);

    }

    private static void createLan(Element element, Node node) {
        if (node.getProps().getLan() != null && node.getProps().getLan().size() > 0) {
            Element languagesElement = element.addElement("Languages");
            List<LanBean> lanList = node.getProps().getLan();
            for (LanBean lan : lanList) {
                Element lanElement = languagesElement.addElement("Language");
                lanElement.addAttribute("type", lan.getType());
                lanElement.addCDATA(lan.getLanguage());
            }
        }
    }

    private static void createRule(Element element, Node node) {
        if (node.getProps().getRule() != null && node.getProps().getRule().size() > 0) {
            Element rulesElement = element.addElement("Rules");
            List<RuleBean> ruleList = node.getProps().getRule();
            for (RuleBean rule : ruleList) {
                Element ruleElement = rulesElement.addElement("Rule");
                ruleElement.addAttribute("type", String.valueOf(rule.getType()));
                ruleElement.addAttribute("fromNodeId", rule.getFromNodeId());
                ruleElement.addCDATA(rule.getRule());
            }
        }
    }


}
