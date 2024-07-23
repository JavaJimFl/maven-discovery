package org.kaib.discover.maven;

public enum RepositoryType {

	ENTERPRISE("enterprise"),

	PERSONAL("personal");

	private final String type;

	private RepositoryType(final String theType) {
		this.type = theType;
	}

	public String getType() {
		return type;
	}

	public static RepositoryType lookup(final String theType) {
		for (RepositoryType value : RepositoryType.values()) {
			if (value.getType().equalsIgnoreCase(theType)) {
				return value;
			}
		}
		
		throw new IllegalArgumentException("Unknown RepositoryType: " + theType);
	}
}
