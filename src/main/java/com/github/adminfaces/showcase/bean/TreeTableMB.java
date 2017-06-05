package com.github.adminfaces.showcase.bean;

import com.github.adminfaces.showcase.model.Document;
import com.github.adminfaces.showcase.service.DocumentService;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by rmpestano on 29/04/17.
 */
@ViewScoped
@Named
public class TreeTableMB implements Serializable {

    private TreeNode root;

    private Document selectedDocument;


    private TreeNode[] selectedNodes;

    @Inject
    private DocumentService service;

    @PostConstruct
    public void init() {
        root = service.createDocuments();
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes1) {
        this.selectedNodes = selectedNodes1;
    }

    public void setService(DocumentService service) {
        this.service = service;
    }

    public Document getSelectedDocument() {
        return selectedDocument;
    }

    public void setSelectedDocument(Document selectedDocument) {
        this.selectedDocument = selectedDocument;
    }
}
