package domain.common;

public class OptionItem {

	public int id;
	private String label;

	public OptionItem(int id, String label) {
		this.id = id;
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

}
