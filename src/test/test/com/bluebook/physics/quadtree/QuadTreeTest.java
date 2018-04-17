/*
package test.com.bluebook.physics.quadtree;

import com.bluebook.physics.CircleCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.quadtree.QuadTree;
import com.bluebook.util.Vector2;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class QuadTreeTest {

    QuadTree qtTree = new QuadTree(new Rectangle(0, 0, 1920, 1080), 2);

    @Test
    void insertInto(){
        // Arrange
        assertEquals(qtTree.isSubdevided, false, "QTTree should not be subdevided yet");
        Collider col = new CircleCollider(new Vector2(1920 / 2, 1080 / 2), 20);

        assertEquals(0, qtTree.query(col).size(), "Excpected size for a qtTree with NO collider");


        qtTree.insert(col);


        assertEquals(1, qtTree.query(col).size(), "Excpected size for a qtTree with 1 collider");

    }

    @Test
    void queryBoundry(){
        assertEquals(qtTree.isSubdevided, false, "QTTree should not be subdevided yet");
        Collider col = new CircleCollider(new Vector2(500, 500), 20);

        assertEquals(0,  qtTree.query(col).size(), "Excpected size for a qtTree with NO collider");


        qtTree.insert(col);


        assertEquals(1, qtTree.query(col).size(), "Excpected size for a qtTree with 1 collider");

        qtTree.colliderQueryWidth = 251;
        qtTree.colliderQueryHeight = 200;

        Collider col2 = new CircleCollider(new Vector2(300, 500), 20);
        qtTree.insert(col2);


        assertEquals(2, qtTree.query(col2).size(), "Excpected size for a qtTree with 2 collider");
    }
}
*/
