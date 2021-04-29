package test.java;

import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.sql2o.*;

import main.java.App;
import main.java.DB;
import spark.Spark;

import org.junit.*;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  //Replacements for the Rules Q2
  @Before
  public void before() {
	    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pokedex_test", "postgres", "5z!67LmIi?U");
  }
  
  @After
  public void after() {
	    try(Connection con = DB.sql2o.open()) {
	      String deletePokemonsQuery = "DELETE FROM pokemons *;";
	      String deleteMovesQuery = "DELETE FROM moves *;";
	      String deleteMovesPokemonsQuery = "DELETE FROM moves_pokemons *;";
	      con.createQuery(deletePokemonsQuery).executeUpdate();
	      con.createQuery(deleteMovesQuery).executeUpdate();
	      con.createQuery(deleteMovesPokemonsQuery).executeUpdate();
	    }
  }
  
  @BeforeClass
  public static void before2() {
	  String[] args = {};
	  App.main(args);
  }

  @AfterClass
  public static void after2() {
	    Spark.stop();
  }

  @Test
  public void rootTest() {//DoesThePokedexExistTest UnitTest
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Pokedex");
  }

  @Test
  public void allPokemonPageIsDisplayed() {//AllPokemonDisplayedTest AcceptanceTest
    goTo("http://localhost:4567/");
    click("#viewDex");
    assertThat(pageSource().contains("Ivysaur"));
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  public void individualPokemonPageIsDisplayed() {//SinglePokemonPageDisplayedTest AcceptanceTest
    goTo("http://localhost:4567/pokepage/6");
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  public void arrowsCycleThroughPokedexCorrectly() {//ArrowsWorkingRightTest UnitTest
    goTo("http://localhost:4567/pokepage/6");
    click(".glyphicon-triangle-right");
    assertThat(pageSource().contains("Squirtle"));
  }

  @Test
  public void searchResultsReturnMatches() {//MatchesForSearchFoundTest UnitTest
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("char");
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  public void searchResultsReturnNoMatches() {//NoMatchesForSearchTest UnitTest
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("x");
    assertThat(pageSource().contains("No matches for your search results"));
  }

}
