package de.gzockoll.pdfcategorizer;

public enum Bookkeeping implements DocumentCategory {
	Invoice("03 Eingangsrechnung"),Bank("01 Bank"),Outgoing("04 Ausgangsrechnung");
	
	private String name;
	
	private Bookkeeping(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}
}
