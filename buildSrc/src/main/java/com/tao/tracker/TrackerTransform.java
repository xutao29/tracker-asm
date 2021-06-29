package com.tao.tracker;

import com.tao.tracker.asm.TrackerClassAdapterVisitor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TrackerTransform extends BaseTransform<TrackerExtension> {

    private final List<String> nameList = new ArrayList<>();
    private final List<String> descriptorList = new ArrayList<>();
    private final List<String> whiteList = new ArrayList<>();

    @Override
    public String getName() {
        return "Tracker";
    }

    @Override
    public void transform(File inputFile, File dstFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(inputFile);
            ClassReader cr = new ClassReader(fis);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            cr.accept(new TrackerClassAdapterVisitor(cw, nameList, descriptorList), ClassReader.EXPAND_FRAMES);

            byte[] newClassBytes = cw.toByteArray();
            fos = new FileOutputStream(dstFile);
            fos.write(newClassBytes);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("执行字节码插桩失败！" + e.getMessage());
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void beforeTransform(TrackerExtension trackerExtension) {
        File insertFile = trackerExtension.getInsertFile();
        File whiteListFile = trackerExtension.getWhiteListFile();
        try {
            if (insertFile != null) {
                Document doc = getDoc(insertFile);

                Element root = doc.getDocumentElement();
                NodeList childNodesInsert = root.getChildNodes();
                for (int i = 0; i < childNodesInsert.getLength(); i++) {
                    Node item = childNodesInsert.item(i);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        for (Node node = item.getFirstChild(); node != null; node = node.getNextSibling()) {
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                if (node.getNodeName().equals("name")) {
                                    nameList.add(node.getFirstChild().getNodeValue());
                                }
                                if (node.getNodeName().equals("descriptor")) {
                                    descriptorList.add(node.getFirstChild().getNodeValue());
                                }
                            }
                        }
                    }
                }
            }

            if (whiteListFile != null) {
                Document doc = getDoc(whiteListFile);

                Element root = doc.getDocumentElement();
                NodeList childNodesInsert = root.getChildNodes();
                for (int i = 0; i < childNodesInsert.getLength(); i++) {
                    Node item = childNodesInsert.item(i);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        if (item.getNodeName().equals("value")) {
                            whiteList.add(item.getFirstChild().getNodeValue());
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException pce) {
            pce.printStackTrace();
        }
    }

    @Override
    public boolean isConnectClassVisitor(String connectPath) {
        return connectPath.endsWith(".class")
                && !connectPath.contains("BuildConfig.class")
                && !connectPath.startsWith("R\\$")
                && !"R.class".equals(connectPath)
                && !isWhiteList(connectPath);
    }

    private boolean isWhiteList(@Nonnull String connectPath) {
        for (int i = 0; i < whiteList.size(); i++) {
            if (connectPath.contains(whiteList.get(i))) {
                return true;
            }
        }
        return false;
    }

    private Document getDoc(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(file);
    }
}
