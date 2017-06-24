package com.zbeninfo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbeninfo.model.Node;
import com.zbeninfo.model.Node.AttrBean;
import com.zbeninfo.model.Node.PropsBean;
import com.zbeninfo.model.Node.PropsBean.LanBean;
import com.zbeninfo.model.Node.PropsBean.PropBean;
import com.zbeninfo.model.Node.PropsBean.RuleBean;
import com.zbeninfo.model.Node.TextBean;
import com.zbeninfo.model.Path;
import com.zbeninfo.model.Path.DotsBean;
import com.zbeninfo.model.Path.TextBean.TextPosBean;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author: Jack
 * @date: 2017/5/24
 */
public class XmlToJsonUtil {

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\tmp\\IVR.xml");
        readXMLFile(file);
    }

    public static Map<String, String> typeMap = new HashMap<>();

    public static JSONObject readXMLFile(File saveFile) throws Exception {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(saveFile);
        Element root = doc.getRootElement();

        List<Element> elementList = root.elements();

        Map<String, Node> nodeMap = new HashMap<>();
        Map<String, Path> pathMap = new HashMap<>();

        addRoot(root, nodeMap);

        if (elementList != null && elementList.size() > 0) {
            for (Element element : elementList) {
                String nodeName = element.getName();
                switch (nodeName) {
                    case "Play":
                        addPlay(element, nodeMap);
                        break;
                    case "Service":
                        addService(element, nodeMap);
                        break;
                    case "Judge":
                        addJudge(element, nodeMap);
                        break;
                    case "TouchKey":
                        addTouchKey(element, nodeMap);
                        break;
                    case "AutoOper":
                        addAutoOper(element, nodeMap);
                        break;
                    case "Handup":
                        addHandup(element, nodeMap);
                        break;
                    case "Relations":
                        addRelations(element, pathMap);
                        break;
                }
            }
        }

        JSONObject json = new JSONObject();
        json.put("states", nodeMap);
        json.put("paths", pathMap);
        return json;

    }


    private static String getType(String elementName) {
        if (typeMap.size() == 0) {
            typeMap.put("Flow", "flowstart");
            typeMap.put("Play", "play");
            typeMap.put("Service", "service");
            typeMap.put("Judge", "judge");
            typeMap.put("TouchKey", "touchkey");
            typeMap.put("AutoOper", "autooper");
            typeMap.put("Handup", "handup");
        }
        return typeMap.get(elementName);
    }

    private static void addRoot(Element root, Map<String, Node> nodeMap) {
        Node flowNode = new Node();

        flowNode.setAttr(getAttr(root.attributeValue("location")));
        flowNode.setId(root.attributeValue("id"));
        flowNode.setType(getType(root.getName()));
        TextBean rootText = new TextBean();
        rootText.setText(root.attributeValue("name"));
        flowNode.setText(rootText);

        PropsBean propsBean = new PropsBean();
        flowNode.setProps(propsBean);

        PropBean propBean = new PropBean();
        propBean.setId(flowNode.getId());
        propBean.setName(root.attributeValue("name"));
        propBean.setNeVoicePath(root.attributeValue("neVoicePath"));
        propBean.setEnVoicePath(root.attributeValue("enVoicePath"));
        propsBean.setProp(propBean);

        nodeMap.put(root.attributeValue("id"), flowNode);
    }

    private static void addPlay(Element element, Map<String, Node> nodeMap) {
        Node node = getCommonNode(element);

        node.getProps().getProp().setBreakFlag(element.attributeValue("breakFlag"));
        List<RuleBean> ruleBeanList = getRule(element);
        List<LanBean> lanBeanList = getLan(element);
        node.getProps().setLan(lanBeanList);
        node.getProps().setRule(ruleBeanList);

        nodeMap.put(element.attributeValue("id"), node);
    }

    private static void addService(Element element, Map<String, Node> nodeMap) {
        Node node = getCommonNode(element);

        node.getProps().getProp().setClassName(element.attributeValue("className"));

        List<RuleBean> ruleBeanList = getRule(element);
        node.getProps().setRule(ruleBeanList);

        nodeMap.put(element.attributeValue("id"), node);
    }

    private static void addJudge(Element element, Map<String, Node> nodeMap) {
        Node node = getCommonNode(element);
        nodeMap.put(element.attributeValue("id"), node);
    }

    private static void addTouchKey(Element element, Map<String, Node> nodeMap) {
        Node node = getCommonNode(element);

        node.getProps().getProp().setTimeOut(element.attributeValue("timeOut"));
        node.getProps().getProp().setFinishkey(element.attributeValue("finishkey"));
        node.getProps().getProp().setRepeatCount(element.attributeValue("repeatCount"));
        node.getProps().getProp().setLength(element.attributeValue("length"));
        node.getProps().getProp().setClassName(element.attributeValue("className"));

        nodeMap.put(element.attributeValue("id"), node);
    }

    private static void addAutoOper(Element element, Map<String, Node> nodeMap) {
        Node node = getCommonNode(element);
        node.getProps().getProp().setAutoOperNum(element.attributeValue("autoOperNum"));
        nodeMap.put(element.attributeValue("id"), node);
    }

    private static void addHandup(Element element, Map<String, Node> nodeMap) {
        Node node = getCommonNode(element);
        node.getProps().getProp().setReason(element.attributeValue("reason"));
        nodeMap.put(element.attributeValue("id"), node);
    }

    private static void addRelations(Element element, Map<String, Path> pathMap) {
        List<Element> relationElementList = element.elements("Relation");
        if (relationElementList != null && relationElementList.size() > 0) {
            int index = 1;
            for (Element relationElement : relationElementList) {
                Path path = new Path();

                path.setFrom(relationElement.attributeValue("nodeId"));
                path.setTo(relationElement.attributeValue("childNodeId"));

                Element dotsElement = relationElement.element("Dots");
                List<DotsBean> dotsBeanList = new ArrayList<>();
                if (dotsElement != null) {
                    List<Element> dotsList = dotsElement.elements("Dot");
                    if (dotsList != null && dotsList.size() > 0) {
                        for (Element dotElement : dotsList) {
                            String x = dotElement.element("x").getText();
                            String y = dotElement.element("y").getText();
                            DotsBean dotsBean = new DotsBean();
                            dotsBean.setX(Integer.parseInt(x));
                            dotsBean.setY(Integer.parseInt(y));
                            dotsBeanList.add(dotsBean);
                        }
                    }
                }
                path.setDots(dotsBeanList);

                Path.TextBean textBean = new Path.TextBean();
                textBean.setText("");
                TextPosBean textPosBean = new TextPosBean();
                textPosBean.setX(0);
                textPosBean.setY(-10);
                textBean.setTextPos(textPosBean);

                path.setText(textBean);

                pathMap.put("path" + index++, path);

            }
        }
    }

    private static List<LanBean> getLan(Element element) {
        List<LanBean> lanBeanList = new ArrayList<>();

        Element languagesElement = element.element("Languages");
        if (languagesElement != null) {
            List<Element> lanElementList = languagesElement.elements("Language");
            if (lanElementList != null && lanElementList.size() > 0) {
                for (Element lanElement : lanElementList) {
                    LanBean lanBean = new LanBean();
                    lanBean.setLanguage(lanElement.getText());
                    lanBean.setType(lanElement.attributeValue("type"));
                    lanBeanList.add(lanBean);
                }
            }
        }
        return lanBeanList;
    }

    private static List<RuleBean> getRule(Element element) {
        List<RuleBean> ruleBeanList = new ArrayList<>();
        Element rulesElement = element.element("Rules");
        if (rulesElement != null) {
            List<Element> ruleElementList = rulesElement.elements("Rule");
            if (ruleElementList != null && ruleElementList.size() > 0) {
                for (int i = 0; i < ruleElementList.size(); i++) {
                    Element ruleElement = ruleElementList.get(i);
                    RuleBean rule = new RuleBean();
                    rule.setIndex(++i);
                    rule.setFromNodeId(ruleElement.attributeValue("fromNodeId"));
                    rule.setType(Integer.parseInt(ruleElement.attributeValue("type")));
                    rule.setRule(ruleElement.getText());
                    ruleBeanList.add(rule);
                }
            }
        }
        return ruleBeanList;
    }

    private static PropBean getCommonProp(Element element) {
        PropBean propBean = new PropBean();

        propBean.setId(element.attributeValue("id"));
        propBean.setName(element.attributeValue("name"));
        propBean.setRemark(element.attributeValue("remark"));

        return propBean;
    }

    private static Node getCommonNode(Element element) {
        Node node = new Node();
        AttrBean attr = getAttr(element);
        node.setAttr(attr);

        node.setId(element.attributeValue("id"));
        node.setType(getType(element.getName()));

        TextBean textBean = new TextBean();
        textBean.setText(element.attributeValue("name"));
        node.setText(textBean);
        PropsBean propsBean = new PropsBean();
        node.setProps(propsBean);

        PropBean propBean = getCommonProp(element);
        propsBean.setProp(propBean);
        return node;
    }

    private static AttrBean getAttr(Element element) {
        Element locationElement = element.element("Location");
        String location = locationElement.getText();
        String JsonStr = "{" + location + "}";
        AttrBean attr = JSONObject.toJavaObject(JSON.parseObject(JsonStr), AttrBean.class);
        return attr;
    }

    private static AttrBean getAttr(String attrStr) {
        String JsonStr = "{" + attrStr + "}";
        AttrBean attr = JSONObject.toJavaObject(JSON.parseObject(JsonStr), AttrBean.class);
        return attr;
    }

}
