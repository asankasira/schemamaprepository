package org.simtech.dmtt.ui.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.simtech.doctransformer.dmt.schemaparser.XsdParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParseUtil {
	
	private  DocumentBuilderFactory dbf;
	private  DocumentBuilder db;
	private  Document doc;
	
	public XMLParseUtil(){
		try{
		  dbf = DocumentBuilderFactory.newInstance();
		  dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); 
	      dbf.setValidating( false );
	      db = dbf.newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String getXMLText(String fileName) {
		
		BufferedReader reader = null;
		StringBuffer xmlText = new StringBuffer("");
		String line = null;
		
//		if ( fileName.substring( fileName.indexOf( '.' ) ).equals( ".xml" ) ||
//        		fileName.substring( fileName.indexOf( '.' ) ).equals( ".xsd" ))
//        {
          try{
			  reader = new BufferedReader( new FileReader( fileName ) );
	          xmlText = new StringBuffer();
	
	          while ( ( line = reader.readLine() ) != null )
	          {
	            xmlText.append( line );
	          } 
	          reader.close();
          }
          catch (Exception e) {
        	  e.printStackTrace();
          }
//        }
		
		return xmlText.toString();
	}
	
	public Node parseXml(String text) {
		
		ByteArrayInputStream byteStream;
		byteStream = new ByteArrayInputStream(text.getBytes());

		try {
			doc = db.parse(byteStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (Node) doc.getDocumentElement();
	}
	
	public Node parseXml(InputStream in) {

		try {
			doc = db.parse(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (Node) doc.getDocumentElement();
	}
	
	   private String getNodeType( Node node )
	   {
	      String type;

	      switch( node.getNodeType() )
	      {
	         case Node.ELEMENT_NODE:
	         {
	            type = "Element";
	            break;
	         }
	         case Node.ATTRIBUTE_NODE:
	         {
	            type = "Attribute";
	            break;
	         }
	         case Node.TEXT_NODE:
	         {
	            type = "Text";
	            break;
	         }
	         case Node.CDATA_SECTION_NODE:
	         {
	            type = "CData section";
	            break;
	         }
	         case Node.ENTITY_REFERENCE_NODE:
	         {
	            type = "Entity reference";
	            break;
	         }
	         case Node.ENTITY_NODE:
	         {
	            type = "Entity";
	            break;
	         }
	         case Node.PROCESSING_INSTRUCTION_NODE:
	         {
	            type = "Processing instruction";
	            break;
	         }
	         case Node.COMMENT_NODE:
	         {
	            type = "Comment";
	            break;
	         }
	         case Node.DOCUMENT_NODE:
	         {
	            type = "Document";
	            break;
	         }
	         case Node.DOCUMENT_TYPE_NODE:
	         {
	            type = "Document type";
	            break;
	         }
	         case Node.DOCUMENT_FRAGMENT_NODE:
	         {
	            type = "Document fragment";
	            break;
	         }
	         case Node.NOTATION_NODE:
	         {
	            type = "Notation";
	            break;
	         }
	         default:
	         {
	            type = "???";
	            break;
	         }
	      }
	      return type;
	   } 
	   
	   public DefaultMutableTreeNode createTreeNode( Node root )
	   {
	      DefaultMutableTreeNode  treeNode = null;
	      String                  name, value;
	      NamedNodeMap            attribs;
	      Node                    attribNode;

	      name = root.getNodeName();
	      value = root.getNodeValue();			

	      treeNode = new DefaultMutableTreeNode( root.getNodeType() == Node.TEXT_NODE ? value : name );

	      attribs = root.getAttributes();
	      if( attribs != null )
	      {
	         for( int i = 0; i < attribs.getLength(); i++ )
	         {
	            attribNode = attribs.item(i);
	            name = attribNode.getNodeName().trim();
	            value = attribNode.getNodeValue().trim();

	            if ( value != null )
	            {
	               if ( value.length() > 0 )
	               {
	                  treeNode.add( new DefaultMutableTreeNode( "[Attribute] --> " + name + "=\"" + value + "\"" ) );
	               } 
	            } 
	         } 
	      } 

	      if( root.hasChildNodes() )
	      {
	         NodeList             children;
	         int                  numChildren;
	         Node                 node;
	         String               data;

	         children = root.getChildNodes();

	         if( children != null )
	         {
	            numChildren = children.getLength();

	            for (int i=0; i < numChildren; i++)
	            {
	               node = children.item(i);
	               if( node != null )
	               {

	                  if( node.getNodeType() == Node.ELEMENT_NODE )
	                  {
	                     treeNode.add( createTreeNode(node) );
	                  } 

	                  data = node.getNodeValue();

	                  if( data != null )
	                  {
	                     data = data.trim();
	                     if ( !data.equals("\n") && !data.equals("\r\n") && data.length() > 0 )
	                     {
	                        treeNode.add(createTreeNode(node));
	                     }
	                  } 
	               } 
	            } 
	         } 
	      } 
	      return treeNode;
	   }  
	   
	  public DefaultMutableTreeNode createXsdTreeNode(String fileName){
		  
		  DefaultMutableTreeNode root = null;
		  
		  XsdParser parser = new XsdParser();
		  Vector data = parser.parseXSD(fileName);
		  
		  for(int i=0; i<data.size(); i++){
			  
			  String[] line = ((String)data.get(i)).split("_");  
			  
			  if (line.length == 0)
					return null;
			  
			  if(root == null)
				  root = new DefaultMutableTreeNode(line[0]);
			  
			  for (int j = 1; j < line.length; j++) {
				  DefaultMutableTreeNode parent = getNodeByPath(root, line, j - 1);
					if (!contains(parent, line[j]))
						parent.add(new DefaultMutableTreeNode(line[j]));
				}
			  
		  }
		  	  
		  return root;
	  }
	  
	  
	  private  DefaultMutableTreeNode getNodeByPath(DefaultMutableTreeNode parent, String[] path, int end) {

			for (int i = 0; i <= end; i++) {
				for (int j = 0; j < parent.getChildCount(); j++)
					if (parent.getChildAt(j).toString().equals(path[i]))
						parent = (DefaultMutableTreeNode) parent.getChildAt(j);
			}
			return parent;
	  }
	  
	  private  boolean contains(DefaultMutableTreeNode parent, String child) {
			for (int k = 0; k < parent.getChildCount(); k++)
				if (parent.getChildAt(k).toString().equals(child))
					return true;
			return false;
	  }
	  
	  public File getFileFromInputStream(InputStream in){
		  
		File temp  = null;  
		  
		try {
			 temp = File.createTempFile("schema", ".xsd");
			 temp.deleteOnExit();
			 
			 OutputStream out = new FileOutputStream(temp);
			 
				int read = 0;
				byte[] bytes = new byte[1024];
			 
				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
			 
			in.close();
			out.flush();
			out.close();
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return temp;
	  }
}
