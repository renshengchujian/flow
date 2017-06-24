package com.zbeninfo.model;

import java.util.List;

/**
 * @author: Jack
 * @date: 2017/5/23
 */
public class Path {


    /**
     * from : 1
     * to : 2
     * dots : [{"x":439,"y":381}]
     * text : {"text":"","textPos":{"x":0,"y":-10}}
     * props : {"text":{"value":""}}
     */

    private String from;
    private String to;
    private TextBean text;
    private PropsBean props;
    private List<DotsBean> dots;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public TextBean getText() {
        return text;
    }

    public void setText(TextBean text) {
        this.text = text;
    }

    public PropsBean getProps() {
        return props;
    }

    public void setProps(PropsBean props) {
        this.props = props;
    }

    public List<DotsBean> getDots() {
        return dots;
    }

    public void setDots(List<DotsBean> dots) {
        this.dots = dots;
    }

    public static class TextBean {

        /**
         * text :
         * textPos : {"x":0,"y":-10}
         */

        private String text;
        private TextPosBean textPos;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public TextPosBean getTextPos() {
            return textPos;
        }

        public void setTextPos(TextPosBean textPos) {
            this.textPos = textPos;
        }

        public static class TextPosBean {

            /**
             * x : 0
             * y : -10
             */

            private int x;
            private int y;

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
        }
    }

    public static class PropsBean {

        /**
         * text : {"value":""}
         */

        private TextBeanX text;

        public TextBeanX getText() {
            return text;
        }

        public void setText(TextBeanX text) {
            this.text = text;
        }

        public static class TextBeanX {

            /**
             * value :
             */

            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    public static class DotsBean {

        /**
         * x : 439
         * y : 381
         */

        private int x;
        private int y;

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
    }
}
