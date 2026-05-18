package common;

public class ComboItem {

	public int id;
	private String label;

	public ComboItem(int id, String label) {
		this.id = id;
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

}
