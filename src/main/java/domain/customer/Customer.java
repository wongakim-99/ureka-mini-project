package domain.customer;

public class Customer {

	private int custId;
	private String name;
	private String phone;
	private String email;
	private Integer age;

	public Customer() {}

	public Customer(int custId, String name, String phone, String email) {
		this.custId = custId;
		this.name = name;
		this.phone = phone;
		this.email = email;
	}

	public Customer(int custId, String name, String phone, String email, Integer age) {
		this.custId = custId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
	}

	public int getCustId()           { return custId; }
	public void setCustId(int v)     { this.custId = v; }
	public String getName()          { return name; }
	public void setName(String v)    { this.name = v; }
	public String getPhone()         { return phone; }
	public void setPhone(String v)   { this.phone = v; }
	public String getEmail()         { return email; }
	public void setEmail(String v)   { this.email = v; }
	public Integer getAge()          { return age; }
	public void setAge(Integer v)    { this.age = v; }

}
