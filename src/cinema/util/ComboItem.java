package cinema.util;

public class ComboItem {

	public String id;
	private String label;

	public ComboItem(String id, String label) {
		this.id = id;
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

}
