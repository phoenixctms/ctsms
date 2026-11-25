package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.TreeNode;

public final class TreeNodeMarkers {

	public static void appendMoreNodes(TreeNode node, Function<Object, Long> childrenCount) {
		long shown = 0l;
		boolean marked = false;
		Iterator<TreeNode> it = new ArrayList<TreeNode>(node.getChildren()).iterator();
		while (it.hasNext()) {
			TreeNode child = it.next();
			if (child instanceof IDVOTreeNode) {
				shown++;
				appendMoreNodes(child, childrenCount);
			} else if (WebUtil.MORE_NODE_TYPE.equals(child.getType())) {
				marked = true;
			} else {
				appendMoreNodes(child, childrenCount);
			}
		}
		if (!marked && node instanceof IDVOTreeNode) {
			Object vo = node.getData();
			Long count = childrenCount.apply(vo);
			if (count != null && count > shown) {
				DefaultTreeNode more = new DefaultTreeNode(WebUtil.MORE_NODE_TYPE,
						new TreeNodeMarker(CommonUtil.getVOId(vo), Messages.getMessage(MessageCodes.TREE_MORE_NODES_LABEL, count - shown)), node);
				more.setSelectable(false);
			}
		}
	}

	public static final int EXPAND_DEPTH = 1;

	public static Integer getExpandMaxInstances(Integer maxInstances, long childrenCount) {
		if (maxInstances != null) {
			return (int) Math.min(Integer.MAX_VALUE, childrenCount + 1l + maxInstances);
		}
		return null;
	}

	public static DefaultTreeNode createAncestorsNode(TreeNode parent, Long id) {
		DefaultTreeNode ancestors = new DefaultTreeNode(WebUtil.ANCESTORS_NODE_TYPE, new TreeNodeMarker(id, Messages.getString(MessageCodes.TREE_ANCESTORS_LABEL)), parent);
		ancestors.setSelectable(false);
		ancestors.setExpanded(true);
		return ancestors;
	}

	public static IDVOTreeNode findNode(TreeNode root, Long id) {
		if (id != null) {
			if (root instanceof IDVOTreeNode && id.equals(CommonUtil.getVOId(root.getData()))) {
				return (IDVOTreeNode) root;
			}
			Iterator<TreeNode> it = root.getChildren().iterator();
			while (it.hasNext()) {
				IDVOTreeNode node = findNode(it.next(), id);
				if (node != null) {
					return node;
				}
			}
		}
		return null;
	}

	public static int detach(TreeNode node) {
		TreeNode parent = node.getParent();
		int index = -1;
		if (parent != null) {
			List<TreeNode> siblings = parent.getChildren();
			for (int i = 0; i < siblings.size(); i++) {
				if (siblings.get(i) == node) {
					index = i;
					break;
				}
			}
			if (index >= 0) {
				siblings.remove(index);
			}
		}
		node.setParent(null);
		return index;
	}

	public static void attach(TreeNode node, TreeNode parent, int index) {
		List<TreeNode> siblings = parent.getChildren();
		if (index < 0 || index > siblings.size()) {
			siblings.add(node);
		} else {
			siblings.add(index, node);
		}
		node.setParent(parent);
	}

	private TreeNodeMarkers() {
	}
}
