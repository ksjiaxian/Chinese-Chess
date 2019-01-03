import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.*;


/** Put your OWN test cases in this file, for all classes in the assignment. */
public class GameTest {

	/**************
	 * Piece tests
	 * ************/
	
	//soldier tests
	@Test
	public void soldierMoveTest() {
		Piece[][] test = new Piece[9][10];
		Soldier soldier = new Soldier(0, 3, true, 1);
		test[0][3] = soldier;
		
		assertTrue(soldier.canMove(0, 4, test));
		
		// doesn't move when given the same spot
		soldier.move(0, 4, test);
		
		// cannot move sideways yet
		assertFalse(soldier.canMove(1, 4, test));
		
		// make sure that the soldier didn't move
		assertTrue(soldier.canMove(0, 5, test));
		soldier.move(0, 5, test);
		
		// crossed the river
		assertTrue(soldier.canMove(0, 6, test));
		soldier.move(0, 6, test);
		assertTrue(soldier.hasCrossedRiver());
		
		// can't move off the board
		assertFalse(soldier.canMove(-1, 6, test));
		
		// but can move sideways
		assertTrue(soldier.canMove(1, 6, test));
		
		// can't move backward
		assertFalse(soldier.canMove(1, 5, test));
		
		//can't move more than one step
		assertFalse(soldier.canMove(1, 8, test));
		
		// if a piece of the same color in the way, cannot move there
		Horse horse = new Horse(1,7, true, 1);
		test[1][7] = horse;
		assertFalse(soldier.canMove(1, 8, test));
	}
	
	//chariot tests
	@Test
	public void chariotMoveTest() {
		Piece[][] test = new Piece[9][10];
		Chariot chariot = new Chariot(0, 0, true, 1);
		test[0][0] = chariot;
		
		assertTrue(chariot.canMove(0, 4, test));
		chariot.move(0, 4, test);
		assertFalse(chariot.canMove(8, 5, test));
		
		// make sure that the chariot didn't move
		chariot.canMove(0, 9, test);
		chariot.move(0, 9, test);
		
		
		// can't move off the board
		assertFalse(chariot.canMove(-1, 9, test));
		
		// make sure that the piece stores its location correctly
		chariot.move(5, 9, test);
		assertEquals(chariot.getX(), 5);
		assertEquals(chariot.getY(), 9);
		
		// if a piece of the same color in the way, cannot move there
		Horse horse = new Horse(1,9, true, 1);
		test[1][9] = horse;
		assertFalse(chariot.canMove(1, 9, test));
	}
	
	// cannon tests
	@Test
	public void cannonMoveTest() {
		Piece[][] test = new Piece[9][10];
		Cannon cannon = new Cannon(1, 2, true, 1);
		test[1][2] = cannon;
		
		// vanilla movement tests
		assertTrue(cannon.canMove(0, 2, test));
		cannon.move(0, 2, test);
		assertTrue(cannon.canMove(0, 4, test));
		cannon.move(0, 4, test);
		assertFalse(cannon.canMove(8, 5, test));
		
		// make sure that the cannon didn't move
		cannon.canMove(0, 9, test);
		cannon.move(0, 9, test);
		
		// can't move off the board
		assertFalse(cannon.canMove(-1, 9, test));
		
		// make sure that the piece stores its location correctly
		cannon.move(5, 9, test);
		assertEquals(cannon.getX(), 5);
		assertEquals(cannon.getY(), 9);
		
		// if a piece of the same color in the way, cannot move there
		Horse horse = new Horse(1,9, true, 1);
		test[1][9] = horse;
		assertFalse(cannon.canMove(1, 9, test));
	}
	
	//general tests
	@Test
	public void generalMoveTest() {
		Piece[][] test = new Piece[9][10];
		General general = new General(4, 0, true, 1);
		test[4][0] = general;
		
		assertTrue(general.canMove(5, 0, test));
		general.move(5, 0, test);
		assertFalse(general.canMove(6, 0, test));
		
		// make sure that the general didn't move
		assertTrue(general.canMove(5, 1, test));
		
		
		// can't move off the board
		general.move(5, 0, test);
		assertFalse(general.canMove(5, -1, test));
		
		//can't move more than one step
		assertFalse(general.canMove(3, 0, test));
		
		//can't move diagonally
		assertFalse(general.canMove(4, 1, test));
		
		// if a piece of the same color in the way, cannot move there
		Horse horse = new Horse(5,1, true, 1);
		test[5][1] = horse;
		assertFalse(general.canMove(5, 1, test));
	}
	
	//advisor tests
	@Test
	public void advisorMoveTest() {
		Piece[][] test = new Piece[9][10];
		Advisor advisor = new Advisor(3, 0, true, 1);
		test[3][0] = advisor;
		
		assertTrue(advisor.canMove(4, 1, test));
		advisor.move(4, 1, test);
		
		// must remain in the bounds of the palace
		assertFalse(advisor.canMove(6, 0, test));
		
		// make sure that the advisor didn't move
		assertTrue(advisor.canMove(5, 2, test));
		advisor.move(5, 2, test);
		
		// can't move off the board
		advisor.move(4, 1, test);
		advisor.move(3, 0, test);
		assertFalse(advisor.canMove(2, -1, test));
		
		//can't move more than one step
		assertFalse(advisor.canMove(5, 2, test));
		
		//can't move perpendicular
		assertFalse(advisor.canMove(4, 0, test));
		
		// if a piece of the same color in the way, cannot move there
		Horse horse = new Horse(4,1, true, 1);
		test[4][1] = horse;
		assertFalse(advisor.canMove(4, 1, test));
	}
	
	//elephant tests
	@Test
	public void elephantMoveTest() {
		Piece[][] test = new Piece[9][10];
		Elephant elephant = new Elephant(2, 0, true, 1);
		test[2][0] = elephant;
		
		assertTrue(elephant.canMove(4, 2, test));
		elephant.move(4, 2, test);
		assertFalse(elephant.canMove(5, 0, test));
		assertFalse(elephant.canMove(4, 0, test));
		
		// make sure that the elephant didn't move
		assertTrue(elephant.canMove(6, 0, test));
		elephant.move(6, 0, test);
		
		// can't move off the board
		assertFalse(elephant.canMove(4, -2, test));
		
		//can't move more than one step
		assertFalse(elephant.canMove(6, 4, test));
		
		//can't move perpendicular
		assertFalse(elephant.canMove(4, 4, test));
		
		// if a piece of the same color in the way, cannot move there
		Horse horse = new Horse(4,2, true, 1);
		test[4][2] = horse;
		assertFalse(elephant.canMove(4, 2, test));
	}
	
	//horse tests
	@Test
	public void horseMoveTest() {
		Piece[][] test = new Piece[9][10];
		Horse horse = new Horse(1, 0, true, 1);
		test[1][0] = horse;
		
		// vanilla movement tests
		assertTrue(horse.canMove(2, 2, test));
		horse.move(2, 2, test);
		assertFalse(horse.canMove(2, 3, test));
		assertFalse(horse.canMove(4, 2, test));
		
		// make sure that the horse didn't move
		assertTrue(horse.canMove(4, 3, test));
		horse.move(4, 3, test);
		assertTrue(horse.canMove(5, 5, test));
		horse.move(5, 5, test);
		
		
		// can't move off the board
		horse.move(7, 4, test);
		assertFalse(horse.canMove(9, 3, test));
		
		//can't move more than one step
		assertFalse(horse.canMove(4, 3, test));
		
		//can't move perpendicular
		assertFalse(horse.canMove(4, 1, test));
		
		// if a piece of the same color in the way, cannot move there
		Elephant elephant = new Elephant(5, 5, true, 1);
		test[5][5] = elephant;
		assertFalse(horse.canMove(5, 5, test));
	}
}