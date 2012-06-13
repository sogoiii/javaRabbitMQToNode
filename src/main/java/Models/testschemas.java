package Models;

import org.bson.types.ObjectId;


public class testschemas {

//	private String id;
	private ObjectId myid;
	
	private String firstname;
	private String lastname;
	private int age;

	public testschemas(){};
	

	public testschemas(ObjectId id, String firstname, String lastname, int age) {
		super();
		this.myid = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.age = age;
	}
	
	
	public ObjectId getId() {
		return myid;
	}
	public void setId(ObjectId id) {
		this.myid = myid;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "User [id=" + myid + ", firstname=" + firstname + ", lastname="
				+ lastname + ", age=" + age + "]";
	}
	
	
	
}
