package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.primefaces.model.TreeNode;

public class IDVOTreeNode implements TreeNode {

	public static final String DEFAULT_TYPE = "default";

	public static <T> IDVOTreeNode findNode(TreeNode root, T search) {
		if (search != null) {
			if (root instanceof IDVOTreeNode) {
				IDVOTreeNode node = (IDVOTreeNode) root;
				IDVO vo = node.getIDVO();
				if (vo != null && vo.equals(new IDVO(search))) {
					return node;
				}
			}
			IDVOTreeNode child = null;
			Iterator<TreeNode> it = root.getChildren().iterator();
			while (it.hasNext()) {
				child = findNode(it.next(), search);
				if (child != null) {
					return child;
				}
			}
			return child;
		} else {
			return null;
		}
	}

	private String type;
	private IDVO data;
	private List<TreeNode> children;
	private TreeNode parent;
	private boolean expanded;
	private boolean selected;
	private boolean selectable;

	public IDVOTreeNode() {
	}

	public IDVOTreeNode(Object vo, TreeNode parent) {
		this.type = DEFAULT_TYPE;
		this.data = new IDVO(vo);
		children = new ArrayList<TreeNode>();
		this.parent = parent;
		if (this.parent != null)
			this.parent.getChildren().add(this);
	}

	public IDVOTreeNode(String type, Object vo, TreeNode parent) {
		this.type = type;
		this.data = new IDVO(vo);
		children = new ArrayList<TreeNode>();
		this.parent = parent;
		if (this.parent != null)
			this.parent.getChildren().add(this);
	}

	public void addChild(TreeNode treeNode) {
		treeNode.setParent(this);
		children.add(treeNode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IDVOTreeNode other = (IDVOTreeNode) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
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
		return data.getVo();
	}

	private IDVO getIDVO() {
		return this.data;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public boolean isLeaf() {
		if (children == null)
			return true;
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
		if (data != null)
			return data.toString();
		else
			return super.toString();
	}
}
