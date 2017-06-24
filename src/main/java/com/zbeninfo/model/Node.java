package com.zbeninfo.model;

import java.util.List;

/**
 * @author: Jack
 * @date: 2017/5/23
 */
public class Node {


    /**
     * type : play id : 2 text : {"text":"play2"} attr : {"x":720,"y":404,"width":50,"height":50}
     * props : {"prop":{"id":2,"name":"play2","breakFlag":"1","remark":""},"rule":[{"index":1,"type":0,"fromNodeId":"1","rule":"1"}],"lan":[{"type":"2","language":"1"}]}
     */

    private String type;
    private String id;
    private TextBean text;
    private AttrBean attr;
    private PropsBean props;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TextBean getText() {
        return text;
    }

    public void setText(TextBean text) {
        this.text = text;
    }

    public AttrBean getAttr() {
        return attr;
    }

    public void setAttr(AttrBean attr) {
        this.attr = attr;
    }

    public PropsBean getProps() {
        return props;
    }

    public void setProps(PropsBean props) {
        this.props = props;
    }

    public static class TextBean {

        /**
         * text : play2
         */

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class AttrBean {

        /**
         * x : 720
         * y : 404
         * width : 50
         * height : 50
         */

        private int x;
        private int y;
        private int width;
        private int height;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class PropsBean {

        /**
         * prop : {"id":2,"name":"play2","breakFlag":"1","remark":""}
         * rule : [{"index":1,"type":0,"fromNodeId":"1","rule":"1"}]
         * lan : [{"type":"2","language":"1"}]
         */

        private PropBean prop;
        private List<RuleBean> rule;
        private List<LanBean> lan;

        public PropBean getProp() {
            return prop;
        }

        public void setProp(PropBean prop) {
            this.prop = prop;
        }

        public List<RuleBean> getRule() {
            return rule;
        }

        public void setRule(List<RuleBean> rule) {
            this.rule = rule;
        }

        public List<LanBean> getLan() {
            return lan;
        }

        public void setLan(List<LanBean> lan) {
            this.lan = lan;
        }

        public static class PropBean {

            /**
             * id : 2
             * name : play2
             * breakFlag : 1
             * remark :
             */

            private String id;
            private String name;
            private String breakFlag;
            private String neVoicePath;
            private String enVoicePath;
            private String className;
            private String timeOut;
            private String finishkey;
            private String repeatCount;
            private String length;
            private String autoOperNum;
            private String reason;
            private String remark;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBreakFlag() {
                return breakFlag;
            }

            public void setBreakFlag(String breakFlag) {
                this.breakFlag = breakFlag;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getNeVoicePath() {
                return neVoicePath;
            }

            public void setNeVoicePath(String neVoicePath) {
                this.neVoicePath = neVoicePath;
            }

            public String getEnVoicePath() {
                return enVoicePath;
            }

            public void setEnVoicePath(String enVoicePath) {
                this.enVoicePath = enVoicePath;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getTimeOut() {
                return timeOut;
            }

            public void setTimeOut(String timeOut) {
                this.timeOut = timeOut;
            }

            public String getFinishkey() {
                return finishkey;
            }

            public void setFinishkey(String finishkey) {
                this.finishkey = finishkey;
            }

            public String getRepeatCount() {
                return repeatCount;
            }

            public void setRepeatCount(String repeatCount) {
                this.repeatCount = repeatCount;
            }

            public String getLength() {
                return length;
            }

            public void setLength(String length) {
                this.length = length;
            }

            public String getAutoOperNum() {
                return autoOperNum;
            }

            public void setAutoOperNum(String autoOperNum) {
                this.autoOperNum = autoOperNum;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }
        }

        public static class RuleBean {

            /**
             * index : 1
             * type : 0
             * fromNodeId : 1
             * rule : 1
             */

            private int index;
            private int type;
            private String fromNodeId;
            private String rule;

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getFromNodeId() {
                return fromNodeId;
            }

            public void setFromNodeId(String fromNodeId) {
                this.fromNodeId = fromNodeId;
            }

            public String getRule() {
                return rule;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }
        }

        public static class LanBean {

            /**
             * type : 2
             * language : 1
             */

            private String type;
            private String language;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }
        }
    }
}
