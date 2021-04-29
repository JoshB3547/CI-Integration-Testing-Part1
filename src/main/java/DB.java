package main.java;
import org.sql2o.*;

public class DB {
  public static Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pokedex", "postgres", "5z!67LmIi?U");
}
