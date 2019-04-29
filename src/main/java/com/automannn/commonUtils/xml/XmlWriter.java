package com.automannn.commonUtils.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author automannn@163.com
 * @time 2019/4/29 11:15
 */
public class XmlWriter {
    List<E> es = new ArrayList<>();

    private XmlWriter(){}

    public static XmlWriter create(){
        return new XmlWriter();
    }

    public XmlWriter element(String name, String text){
        E e = new TextE(name, text);
        es.add(e);
        return this;
    }

    public XmlWriter element(String name, Number number){
        E e = new NumberE(name, number);
        es.add(e);
        return this;
    }

    public XmlWriter element(String parentName, String childName, String childText){
        return element(parentName, new TextE(childName, childText));
    }

    public XmlWriter element(String parentName, String childName, Number childNumber){
        return element(parentName, new NumberE(childName, childNumber));
    }

    public XmlWriter element(String parentName, Object... childPairs){
        if (childPairs.length % 2 != 0){
            throw new RuntimeException("var args's length must % 2 = 0");
        }
        E parent = new TextE(parentName, null);
        List<E> children = new ArrayList<>();
        E child;
        for (int i=0; i<childPairs.length ; i=i+2){
            if (childPairs[i+1] instanceof Number){
                child = new NumberE((String)childPairs[i], (Serializable)childPairs[i+1]);
            } else {
                child = new TextE((String)childPairs[i], (Serializable)childPairs[i+1]);
            }
            children.add(child);
        }
        parent.children = children;
        es.add(parent);
        return this;
    }

    public XmlWriter element(String parentName, E child){
        E e = new TextE(parentName, null);
        e.children = Arrays.asList(child);
        es.add(e);
        return this;
    }

    public XmlWriter element(String parentName, List<E> children){
        E e = new TextE(parentName, null);
        e.children = children;
        es.add(e);
        return this;
    }

    public E newElement(String parentName,  Object... childPairs){
        E parent = new TextE(parentName, null);
        List<E> children = new ArrayList<>();
        E child;
        for (int i=0; i<childPairs.length ; i=i+2){
            if (childPairs[i+1] instanceof Number){
                child = new NumberE((String)childPairs[i], (Serializable)childPairs[i+1]);
            } else {
                child = new TextE((String)childPairs[i], (Serializable)childPairs[i+1]);
            }
            children.add(child);
        }
        parent.children = children;
        return parent;
    }

    public String build(){
        return buildElements();
    }

    private String buildElements() {

        StringBuilder xml = new StringBuilder();

        xml.append("<xml>");

        if (es != null && es.size() > 0){
            for (E e : es){
                xml.append(e.render());
            }
        }

        xml.append("</xml>");

        return xml.toString();
    }

    public static abstract class E {

        String name;

        Object text;

        List<E> children;

        public E(String name, Object text) {
            this.name = name;
            this.text = text;
        }

        protected abstract String render();
    }

    public static final class TextE extends E {

        public TextE(String name, Serializable content) {
            super(name, content);
        }

        @Override
        protected String render() {
            StringBuilder content = new StringBuilder();
            content.append("<").append(name).append(">");

            if (text != null){
                content.append("<![CDATA[").append(text).append("]]>");
            }

            if (children != null && children.size() > 0){
                for (E child : children){
                    content.append(child.render());
                }
            }

            content.append("</").append(name).append(">");
            return content.toString();
        }
    }

    public static final class NumberE extends E {

        public NumberE(String name, Serializable content) {
            super(name, content);
        }

        @Override
        protected String render() {
            StringBuilder content = new StringBuilder();
            content.append("<").append(name).append(">")
                    .append(text)
                    .append("</").append(name).append(">");
            return content.toString();
        }
    }
}
