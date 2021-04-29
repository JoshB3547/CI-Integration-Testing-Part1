package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sql2o.*;

import main.java.Move;
import main.java.Pokemon;

@RunWith(Parameterized.class)
public class PokemonTest {
	public Pokemon currentpokemon;
	public int expectedHealth;
	public String moveType;
	
	
  @Rule
  public DatabaseRule database = new DatabaseRule();
  
  @Parameterized.Parameters
  public static Collection defendParamenters() {
     return Arrays.asList(new Object[][] {
        { new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false), 400, "Fire" },
        { new Pokemon("Charmander", "Fire", "None", "A cute Snake", 50.0, 12, 16, false), 100, "Water" },
        { new Pokemon("Geodude", "Rock", "Ground", "A cute rock", 50.0, 12, 16, false), 400, "Rock" },
        { new Pokemon("Charizard", "Fire", "Flying", "A cute dragon", 50.0, 12, 16, false), 300, "Ghost" },
        { new Pokemon("Butterfree", "Bug", "Flying", "A cute Bug", 50.0, 12, 16, false), -300, "Rock" }
     });
  }
  
  

  public PokemonTest(Pokemon currentpokemon, int expectedHealth, String moveType) {
	super();
	this.currentpokemon = currentpokemon;
	this.expectedHealth = expectedHealth;
	this.moveType = moveType;
}



  @Test
  public void Pokemon_instantiatesCorrectly_true() {//PokemonInstatiationTest UnitTest
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertEquals(true, myPokemon instanceof Pokemon);
  }

  @Test
  public void getName_pokemonInstantiatesWithName_String() {//PokemonNameInstatiationTest UnitTest
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertEquals("Squirtle", myPokemon.getName());
  }

  @Test
  public void all_emptyAtFirst() {//AllPokemonEmptyTest UnitTest
    assertEquals(Pokemon.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfPokemonAreTheSame_true() {//PokemonEqualityTest UnitTest
    Pokemon firstPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    Pokemon secondPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertTrue(firstPokemon.equals(secondPokemon));
  }

  @Test
  public void save_savesPokemonCorrectly_1() {//SavingPokemonTest UnitTest
    Pokemon newPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    newPokemon.save();
    assertEquals(1, Pokemon.all().size());
  }

  @Test
  public void find_findsPokemonInDatabase_true() {//PokemonLocatedTest IntegrationTest
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    Pokemon savedPokemon = Pokemon.find(myPokemon.getId());
    assertTrue(myPokemon.equals(savedPokemon));
  }

  @Test
  public void addMove_addMoveToPokemon() {//MovesAddedToPokemonTest IntegrationTest
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    myPokemon.addMove(myMove);
    Move savedMove = myPokemon.getMoves().get(0);
    assertTrue(myMove.equals(savedMove));
  }

  @Test
  public void delete_deleteAllPokemonAndMovesAssociations() {//AllPokemonAndMoveAssociationsDeletedTest IntegrationTest
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    Move myMove = new Move("Bubble", "Water", 50.0, 100);
    myMove.save();
    myPokemon.addMove(myMove);
    myPokemon.delete();
    assertEquals(0, Pokemon.all().size());
    assertEquals(0, myPokemon.getMoves().size());
  }

  @Test
  public void searchByName_findAllPokemonWithSearchInputString_List() {//AllPokemonFoundWithStringTest IntegrationTest
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    assertEquals(myPokemon, Pokemon.searchByName("squir").get(0));
  }

  //should run multiple times for the parameters
  @Test
  public void fighting_damagesDefender() {//PokemonDamageableTest IntegrationTest
    Pokemon myPokemon = currentpokemon;
    myPokemon.save();
    myPokemon.hp = 500;
    Move myMove = new Move("Random Attack", moveType, 50.0, 100);
    myMove.attack(myPokemon);
    System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
        System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
        System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
    assertEquals(expectedHealth, myPokemon.hp);
  }

}
