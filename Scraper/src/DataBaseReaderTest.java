import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class DataBaseReaderTest {

	@Test
	public void testGetAllDrinksTwo() {
		ArrayList<Drink> list1 = DataBaseReader.getAllDrinks();
		ArrayList<Drink> list2 = DataBaseReader.getAllDrinksTwo();
		assertTrue(!list1.isEmpty());
		assertTrue(!list2.isEmpty());
		int iterationLimit = 20000;
		System.out.println(list1.size());
		System.out.println(list2.size());
		for (int i = 0; i < list1.size() && i < list2.size() && i < iterationLimit; i++)
		{
			findMatch(list1, list2, i);
		}
	}

	private void findMatch(ArrayList<Drink> list1, ArrayList<Drink> list2, int index1) 
	{
		Drink drink1 = list1.get(index1);
		boolean pass = false;
		for (int i = 0; i < list2.size(); i++)
		{
			Drink currDrink = list2.get(i);
			int id1 = drink1.getId();
			int id2 = currDrink.getId();
			if (id1 == id2)
			{
				//System.out.println("ID " + drink1.getId() + " match for index " + index1 + " and " + list2.indexOf(currDrink));
				//int index2 = list2.indexOf(currDrink);
				int index2 = i;
				if (id1 != index1 || id1 != index2)
				{
					System.out.println("drink1 id is " + id1 + " index1 is " + index1 + "\t\tdrink2 id is " + id2 + " and index2 is " + index2);
				}
				pass = true;
			}
		}
		assertTrue(pass);
	}

}
