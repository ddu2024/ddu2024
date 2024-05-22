package org.nbme.dwbi.synthetic.util;

import java.util.LinkedHashSet;
import java.util.Objects;

public class TreeNode {

	private String name;
	private LinkedHashSet<TreeNode> childNodes;
	private LinkedHashSet<TreeNode> parentNodes = new LinkedHashSet<TreeNode>();

	public TreeNode(String name) {
		this.name = name;
		this.childNodes = new LinkedHashSet<>();
	}

	public void addChild(TreeNode childNode) {
		this.childNodes.add(childNode);
		childNode.addParent(this);
	}

	//https://www.baeldung.com/java-breadth-first-search
	//	  public void showTreeNodes() {
	//	    BreathFirstSearchPrintTreeNodes.printNodes(this);
	//	  }

//	public String getFullQualifiedName() {
//		return container.getName() + (field != null ? "." + field.getName() : "");
//	}

	public LinkedHashSet<TreeNode> getChildNodes() {
		return childNodes;
	}
	
	public LinkedHashSet<TreeNode> getParentNodes() {
		return parentNodes;
	}

	private void addParent(TreeNode parent) {
		parentNodes.add(parent);
	}

	public String getName() {
		return name;
	}

//	public Field getField() {
//		return field;
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(container, field);
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeNode other = (TreeNode) obj;
		return Objects.equals(childNodes, other.childNodes) && Objects.equals(name, other.name)
				 && Objects.equals(parentNodes, other.parentNodes);
	}
}
