import java.util.*;
import java.util.Collections;
import java.util.LinkedList;


public class BTree {

  //exists only to allow LeafNode and RootNode to fit within the same linked list;
  //vitrually zero shared properties exist between the two subclasses
  static abstract class Node{
    //always overriden by subclass polymorphic functions
    Boolean sidewaysSearch(LinkedList<Node> nodes, Integer searchFor, int i){
      System.out.println("Error: This method is being should never be called: Node.sidewaysSearch");
      return false;
    }
    //always overriden by RootNode polymorphic function
    Boolean completeSearch(Integer searchFor){
      System.out.println("Error: This method is being should never be called: Node.completeSearch");
      return false;
    }
    //pointless Constructor
    Node(){}
  }

  //Holds any amount of other nodes whoes values fit within the range set by min and max
  static class RootNode extends Node{
    private int min, max;
    private LinkedList<Node> nodes;

    //searches depth first and then bredth. Calls sidewaysSearch and is in turn called by it
    @Override
    Boolean completeSearch(Integer searchFor){
      //checking searchFor fits in range
      if (searchFor > this.max || searchFor < this.min){
        return false;
      }

      //runs sidewaysSearch on first element of linked list
      //third argument of sidewaysSearch is effectively an iterator;
      //it allows the progam to navigate the linked list by index
      Node ptr = this.nodes.getFirst();
      return ptr.sidewaysSearch(nodes, searchFor, 0);
    }

    //If a RootNode is found on a breadth-search, a depth search is immediately
    // performed on it before continuing the breadth-search
    @Override
    Boolean sidewaysSearch(LinkedList<Node> nodes, Integer searchFor, int i){
        if(this.completeSearch(searchFor)==true){ //depth search
          return true;
        } else if (nodes.lastIndexOf(nodes.getLast()) <= i){
          return false;
          //^checking that I'm not about to go out of bounds
          //^nodes.lastIndexOf(nodes.getLast()) is a crude way
          // of getting around the fact that .size() wasn't working for me
        } else {
          Node ptr = nodes.get(i+1);
          return ptr.sidewaysSearch(nodes, searchFor, i+1);
          //^recursively looks at every item in the list,
          // using polymorphism to differentiate between Node types
        }
      }

    //Constructor
    RootNode(int min, int max, LinkedList<Node> nodes){
      this.min = min;
      this.max = max;
      this.nodes = nodes;

    }
  }

  //holds an array of Integer objects, can search itself and ask other nodes to do likewise
  static class LeafNode extends Node{
    private LinkedList<Integer> values;

    @Override
    Boolean sidewaysSearch(LinkedList<Node> nodes, Integer searchFor, int i){
        //Base case: number found
        if (this.values.contains(searchFor)){
          return true;
        //checking size of list; see RootNode.sidewaysSearch for more details
        } else if (nodes.lastIndexOf(nodes.getLast()) <= i) {
          return false;
        } else {
          Node ptr = nodes.get(i+1);
          return ptr.sidewaysSearch(nodes, searchFor, i+1);
          //^recursively looks at every item in the list,
          // using polymorphism to differentiate between Node types
        }
      }

      //should never be called; exists here for error-checking purposes
      @Override
      Boolean completeSearch(Integer searchFor){
        System.out.println("Error: This method is being should never be called: LeafNode.completeSearch");
        return false;
      }

    //Constructor
    LeafNode(LinkedList<Integer> values){
        this.values = values;
    }
  }

    public static void main(String args[]){

        LinkedList<Integer> nums = new LinkedList<Integer>();
        LinkedList<Integer> othernums = new LinkedList<Integer>();
        LinkedList<Integer> smothernums = new LinkedList<Integer>();

        for (int i = 0; i<10; i++){
          nums.add(i);//0-9
          othernums.add(i+10);//10-19
          smothernums.add(i*10);//0-90, count by tens
        }

        LeafNode leef = new LeafNode(nums);
        LeafNode beef = new LeafNode(othernums);
        LeafNode reef = new LeafNode(smothernums);

        LinkedList<Node> nodeabode = new LinkedList<Node>();

        nodeabode.add(leef);
        nodeabode.add(beef);

        RootNode root = new RootNode(0, 20, nodeabode);

        LinkedList<Node> nodecommode = new LinkedList<Node>();

        nodecommode.add(root);
        nodecommode.add(reef);

        RootNode head = new RootNode(0, 100, nodecommode);

        System.out.println(head.completeSearch(1));
        System.out.println(head.completeSearch(19));
        System.out.println(head.completeSearch(50));
        System.out.println(head.completeSearch(21));
        System.out.println(head.completeSearch(91));
    }
}
