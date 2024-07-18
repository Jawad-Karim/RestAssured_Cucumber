package utils;

import org.apache.commons.lang3.RandomStringUtils;

import com.github.javafaker.Faker;

/* Need to add the dependency to POM for fake data
  	<groupId>com.github.javafaker</groupId>
	<artifactId>javafaker</artifactId>
	
	* for random String Utils data generating
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-lang3</artifactId>
 */

public class RandomFakeDataGenerator {

	public static void main(String[] args) {
		
		Faker faker = new Faker();
		
		//generate name
		String fName = faker.name().firstName();
		String lName = faker.name().lastName();
		String fullName = faker.name().fullName();
		
		//generate address
		String street = faker.address().streetAddress(); //street name with number
		String streetName = faker.address().streetName();
		String houseNo = faker.address().streetAddressNumber();
		String city = faker.address().city();
		String state1 = faker.address().state();
		String zip = faker.address().zipCode();
		String contri = faker.address().country();
		
		String phone = faker.phoneNumber().cellPhone();
		System.out.println(contri);
		
		//generate random alpha and numeric data
		String alfa = RandomStringUtils.randomAlphabetic(4); 		//only alphabetic
		String num = RandomStringUtils.randomNumeric(5);			//only numeric
		String alfaNum = RandomStringUtils.randomAlphanumeric(5);	//alphabetic and numeric
		
		String data = RandomStringUtils.randomAlphabetic(10);
		String email = data+"@gmail.com";
		System.out.println("email: "+email);
		
		System.out.println(alfa);
		System.out.println(num);
		System.out.println(alfaNum);

	}

}
