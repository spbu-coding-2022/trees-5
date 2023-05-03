# trees-5

---

> `trees-5` - This is an application that allows you to visualize the work of such types of binary trees as
> [Simple Binary Search Trees](https://en.wikipedia.org/wiki/Binary_search_tree), [AVL Trees](https://en.wikipedia.org/wiki/AVL_trees),
> [Red-Black Trees](https://en.wikipedia.org/wiki/Red–black_tree).

### The application supports saving trees to databases
- Simple Binary Search Trees in **Json** flat file
- AVL Trees in **SQLite** 
- Red-Black Trees in **Neo4j**

---


## Getting started


### Build

To build and run this application locally, you'll need Git, Gradle and JDK installed on your computer. From your command
line:

```bash
# Clone this repository
git clone https://github.com/spbu-coding-2022/trees-5.git

# Go into the repository
cd trees-5

# Build
./gradlew assemble      
# Or use `./gradlew build` if you want to test and lint app

# Run the app
./gradlew run
```

### Neo4j database
>This database requires a separate connection to store in it Red-Black Trees

TO-DO __A brief guide__ \
If you got trouble during installation or for more information visit [site](https://neo4j.com/docs/operations-manual/current/installation/)

---

## More information about the app

### How to get started with tree

First, select the type of binary tree you will work with, then start entering values of keys to insert into the tree. If you want to insert a key and a value, then you need to type the value of the key in the input line first, then use the separator in the form ';' and write the value to be stored with the key. 

### Working with a binary tree

You can work on all third types of binary trees at the s~~~~ame time.
What type the tree will have is determined by the first entered value, if it is a string value, then the comparison will be defined for strings, otherwise the tree will be numeric and after string keys simply will not be inserted into it. 
To show the value of the nodes, press the left shift, pressing again, the keys will show again.
To delete or find a node with a specific key, you must enter the key and click the remove or find button.
If you want to hide the tree, then this can be done by pressing the space bar.

## Screenshots
![avl-example](https://sun9-21.userapi.com/impg/XkXWgKUqZOZSkT0PjiyAsERwKfYLCe8cR5UMgA/j7LzFEnBzpI.jpg?size=1280x750&quality=95&sign=119cf3967c051167d8036c64db8847b9&type=album)
![rbt-example](https://sun9-78.userapi.com/impg/FLvdcvXfdG24hI8_dbkY4BzhB87dLmCb0MzJ5A/_oPqObEW78U.jpg?size=1280x750&quality=95&sign=092fd209f0c2e7496ae780ea6caf3801&type=album)

~~~~
---

### License

Distributed under the GNU GPL v3.0 License. See [LICENSE](LICENSE) for more information.
