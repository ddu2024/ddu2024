package org.nbme.dwbi.synthetic.model;

import java.util.ArrayList;
import java.util.Objects;

public class Container {
	private ContainerType containerType;
	private String containerName;
	private ArrayList<Field> fields = new ArrayList<Field>();

	public ContainerType getContainerType() {
		return containerType;
	}

	public void setContainerType(ContainerType containerType) {
		this.containerType = containerType;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String name) {
		this.containerName = name;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	@Override
	public int hashCode() {
		return Objects.hash(containerType, fields, containerName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Container other = (Container) obj;
		return containerType == other.containerType && Objects.equals(fields, other.fields)
				&& Objects.equals(containerName, other.containerName);
	}
}
