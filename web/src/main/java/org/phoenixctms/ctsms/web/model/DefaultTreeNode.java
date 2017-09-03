package org.phoenixctms.ctsms.web.model;

/*
 * Copyright 2009 Prime Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.TreeNode;

public class DefaultTreeNode implements TreeNode {

	public static final String DEFAULT_TYPE = "default";
	private String type;
	private Object data;
	private List<TreeNode> children;
	private TreeNode parent;
	private boolean expanded;
	private boolean selected;
	private boolean selectable;

	@Deprecated
	public DefaultTreeNode(Object data) {
		this.type = DEFAULT_TYPE;
		this.data = data;
		children = new ArrayList<TreeNode>();
	}

	public DefaultTreeNode(Object data, TreeNode parent) {
		this.type = DEFAULT_TYPE;
		this.data = data;
		children = new ArrayList<TreeNode>();
		this.parent = parent;
		if (this.parent != null) {
			this.parent.getChildren().add(this);
		}
	}

	public DefaultTreeNode(String type, Object data, TreeNode parent) {
		this.type = type;
		this.data = data;
		children = new ArrayList<TreeNode>();
		this.parent = parent;
		if (this.parent != null) {
			this.parent.getChildren().add(this);
		}
	}

	public void addChild(TreeNode treeNode) {
		treeNode.setParent(this);
		children.add(treeNode);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public List<TreeNode> getChildren() {
		return children;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public boolean isLeaf() {
		if (children == null) {
			return true;
		}
		return children.size() == 0;
	}

	@Override
	public boolean isSelectable() {
		return selectable;
	}

	@Override
	public boolean isSelected() {
		return this.selected;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
		if (parent != null) {
			parent.setExpanded(expanded);
		}
	}

	@Override
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	@Override
	public void setSelectable(boolean value) {
		selectable = value;
	}

	@Override
	public void setSelected(boolean value) {
		this.selected = value;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		if (data != null) {
			return data.toString();
		} else {
			return super.toString();
		}
	}
}
