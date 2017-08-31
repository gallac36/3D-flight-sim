

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;

public class BasicScene extends CustomFrame 
{
	
	
	public static void main(String[] args) {
		new BasicScene();
	}
	

  
  public Canvas3D canvas;
  public TransformGroup vtg;
  public View view;
  
  public BasicScene()
  {
    getContentPane().setLayout(new BorderLayout());

    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

    // Create a Canvas3D object and add it to the frame
    canvas = new Canvas3D(config);
    canvas.addMouseListener(this);
    getContentPane().add(canvas, BorderLayout.CENTER);
    	
    // Create a SimpleUniverse object to manage the "view" branch
    SimpleUniverse u = new SimpleUniverse(canvas);
    u.getViewingPlatform().setNominalViewingTransform();
    vtg = u.getViewingPlatform().getViewPlatformTransform();
    view = u.getViewer().getView();
   // view.setProjectionPolicy(VIRTUAL_SCREEN);

   
     
    view.setFrontClipDistance(0.001);

    // Add the "content" branch to the SimpleUniverse
    setSize(256, 256);
    BranchGroup scene = createContentBranch();
    u.addBranchGraph(scene);
    
    
    setVisible(true);
    }
  
  public BranchGroup createContentBranch() 
  {
    BranchGroup root = new BranchGroup();
  
    // Create the transform group
    TransformGroup transformGroup = new TransformGroup();
    transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(transformGroup);
    
    root.compile();

    return root;
  }
  
 

}
