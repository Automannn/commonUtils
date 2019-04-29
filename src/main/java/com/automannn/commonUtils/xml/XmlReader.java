package com.automannn.commonUtils.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author automannn@163.com
 * @time 2019/4/29 11:11
 */
public class XmlReader {
    private Document document;

    private XmlReader(){}

    public static XmlReader create(String xml){
        return create(xml, "UTF-8");
    }

    public static XmlReader create(String xml, String encode){
        try {
            return create(new ByteArrayInputStream(xml.getBytes(encode)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static XmlReader create(InputStream inputStream){
        XmlReader readers = new XmlReader();
        try {
            readers.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Xmls create fail", e);
        }
        return readers;
    }

    public Node getNode(String tagName){
        NodeList nodes = document.getElementsByTagName(tagName);
        if (nodes.getLength() <= 0){
            return null;
        }
        return nodes.item(0);
    }

    public NodeList getNodes(String tagName){
        NodeList nodes = document.getElementsByTagName(tagName);
        if (nodes.getLength() <= 0){
            return null;
        }
        return nodes;
    }

    public String getNodeStr(String tagName){
        Node node = getNode(tagName);
        return node == null ? null : node.getTextContent();
    }

    public Integer getNodeInt(String tagName){
        String nodeContent = getNodeStr(tagName);
        return nodeContent == null ? null : Integer.valueOf(nodeContent);
    }

    public Long getNodeLong(String tagName){
        String nodeContent = getNodeStr(tagName);
        return nodeContent == null ? null : Long.valueOf(nodeContent);
    }

    public Float getNodeFloat(String tagName){
        String nodeContent = getNodeStr(tagName);
        return nodeContent == null ? null : Float.valueOf(nodeContent);
    }
}
