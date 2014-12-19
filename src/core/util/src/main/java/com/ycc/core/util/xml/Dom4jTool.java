package com.ycc.core.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import com.ycc.core.util.def.CommonDef;
import com.ycc.core.util.validator.StringUtil;

public class Dom4jTool {
	private static Map<String,Document> DOCS = new HashMap<String,Document>();
	public static Document parseFromFile(String file){
		if(StringUtil.isEmpty(file)){
			return null;
		}
		if(DOCS.containsKey(file)){
			return DOCS.get(file);
		}
		File f = new File(file);
		if(!f.exists()){
			return null;
		}else{
			FileInputStream fis = null;
			try {
				 fis = new FileInputStream(f);
				 Document doc = Dom4jTool.parseFromStr(IOUtils.toString(fis,CommonDef.CHARSET));
				 DOCS.put(file, doc);
				return doc;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	public static Document parseFromStr(String xml){
		try {
			Document document = DocumentHelper.parseText(xml);
			return document;
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
		return null;
	}
	public static String getValue(Node doc, String xpath) {
		try {
			Node node = doc.selectSingleNode(xpath);
			if(node!=null){
				return node.getText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	public static Node getSingleNode(Node doc, String xpath) {
		try {
			Node node = doc.selectSingleNode(xpath);
			if(node!=null){
				return node;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static List getNodes(Node doc,String xpath){
		try {
			return doc.selectNodes(xpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String text = "<members><member><name>ycc</name></member><a>3</a></members>"; 
		Document doc = Dom4jTool.parseFromStr(text);
		System.out.println(Dom4jTool.getValue(doc, "/members/member/name"));
		Node node = Dom4jTool.getSingleNode(doc, "/members[member/name='ycc']/a");
		System.out.println(node.getText());
	}
}
