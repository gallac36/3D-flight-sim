

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;


public class Chopper extends BasicScene implements KeyListener, Runnable
{
  
	private static final long serialVersionUID = 1L;
	public AxisAngle4f rotation = new AxisAngle4f(0.0f, 1.0f, 0.0f, 0.0f);
	public float angle = 0.0f;
    public float zAngle = 0.0f;
	
 	 Graphics2D graphics2d;
 	 String altitude = new String();
 	Appearance appearanceDash;
 	Texture dashTexture = null;
	BufferedImage dash;
	public float zspeed, xspeed;
	public float throttle;
	public float posx, posz, speed, speed1;
	
	Appearance appearance ;
  
  public BranchGroup createContentBranch()
  {
	  BranchGroup root = new BranchGroup();
	  BranchGroup viewing = new BranchGroup();
	  Texture grassTexture = null;
	  
	  	///////Ground
	    try
	    {
	      //load the ground texture
	      TextureLoader loader1 = new TextureLoader("resources/grass1.jpg", this);
	      grassTexture = loader1.getTexture();  
	    }
	    catch(Exception e){System.out.println(e.toString());}

	    Appearance groundAppearance = new Appearance();
	    groundAppearance.setTexture(grassTexture);
	    
	    float[] groundCoords = {
	    		-20.0f, -0.250f,  20.0f, 
	            20.0f, -0.250f,  20.0f,
	            20.0f,  -0.250f,  -20.0f,
	            -20.0f,  -0.250f,  -20.0f,
	        };
	   

	        
	    // Define the ground texture coordinates for the vertices
	    float[] texCoords1 = {0.0f, 0.0f,
	          100.0f, 0.0f,
	          100.0f,  100.0f,
	          0.0f, 100.0f
	          }; 
	   
	    // Create a geometry array from the specified coordinates with quad array
	    GeometryArray geometryArray1 = new QuadArray(12,
	    		GeometryArray.COORDINATES|GeometryArray.TEXTURE_COORDINATE_2);

	    geometryArray1.setCoordinates(0, groundCoords);
	    geometryArray1.setTextureCoordinates(0,0,texCoords1);

	    Shape3D groundShape = new Shape3D(geometryArray1, groundAppearance);
	    root.addChild(groundShape);
	    
	    
	    //////////////////////Dash board
	    dash = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_ARGB);
	    graphics2d = dash.createGraphics();
	    graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    graphics2d.setColor(Color.BLACK);
	    graphics2d.fillRect(0,0,1000,400);
	    graphics2d.setColor(Color.WHITE);
	    
	    graphics2d.setFont(new Font("default",Font.PLAIN, 40 ));
	    String speedstr = Float.toString(speed1);
	    graphics2d.drawString(speedstr+"kph",10, 50);
	   
	    //dashTexture = loader.getTexture();
	    Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGBA, 1000, 400);
	    texture.setImage(0, new ImageComponent2D(ImageComponent2D.FORMAT_RGBA, dash));
	    
	    appearanceDash = new Appearance();
	    appearanceDash.setTexture(texture);
	   
	    float[] dashCoordinates = {
	    		-0.4f, -0.4f,  -1.0f, 
	            0.4f, -0.4f,  -1.0f,
	            0.4f,  -0.185f,  -1.0f,
	            -0.4f,  -0.185f,  -1.0f,
	        };
	    float[] texCoords2 = {0.0f, 0.0f,
		          1.0f, 0.0f,
		          1.0f,  1.0f,
		          0.0f, 1.0f
		          }; 
	 // Create a geometry array from the specified coordinates with quad array
	    GeometryArray geometryArray2 = new QuadArray(12,
	    		GeometryArray.COORDINATES|GeometryArray.TEXTURE_COORDINATE_2);
	    geometryArray2.setCoordinates(0, dashCoordinates);
	    geometryArray2.setTextureCoordinates(0,0,texCoords2);

	    Shape3D shapeDash = new Shape3D(geometryArray2, appearanceDash);
	    
	    viewing.addChild(shapeDash);

	   
	    ///////////helipad//////////////////
	    Texture heliTexture = null;
	    try
	    {
	      //load the helipad texture
	      TextureLoader loader2 = new TextureLoader("resources/helipad.jpg", this);
	      heliTexture = loader2.getTexture();
	    }
	    catch(Exception e){System.out.println(e.toString());}
	    
	     
	    Appearance appearanceHeli = new Appearance();
	    
	    //set texture to modulate to reflect light
	    TextureAttributes ta=new TextureAttributes();
	    ta.setTextureMode(TextureAttributes.MODULATE);
	    appearanceHeli.setTextureAttributes(ta);
	    
	    appearanceHeli.setTexture(heliTexture);
	    
	    Material heliMaterial = new Material( );
	    heliMaterial.setAmbientColor(0.0f, 0.0f, 0.0f);
	    heliMaterial.setDiffuseColor(1.0f,1.0f,1.0f);
	    heliMaterial.setShininess(100.0f);
	    appearanceHeli.setMaterial( heliMaterial );
	    
	    Box helibox = new Box(0.75f, 0.001f, 0.75f, Box.GENERATE_NORMALS|Box.GEOMETRY_NOT_SHARED|Box.GENERATE_TEXTURE_COORDS, appearanceHeli);
	    Transform3D heliTrans = new Transform3D();
	    heliTrans.setTranslation(new Vector3f(0.0f, -0.245f, 0.0f));
	    
	    TransformGroup helitg = new TransformGroup(heliTrans);
	  
	    helitg.addChild(helibox);
	    
	    root.addChild(helitg);
	    
/////////////////////////////chopper/////////////
    		
    		
    		Transform3D trans = new Transform3D();
    		trans.rotY(3.1415);
    		trans.setTranslation(new Vector3d(0.0f, -0.19f ,-2.0f));
    		
    		trans.setScale(0.2);
    		TransformGroup tg1 = new TransformGroup(trans);

    		// load the object file
    	    Scene scene = null;
    	   // Shape3D shape = null;

    	    // read in the geometry information from the data file
    	    ObjectFile objFileloader = new ObjectFile( ObjectFile.RESIZE );

    	    try
    	    {
    	      scene = objFileloader.load( "resources/comanche.obj" );
    	    }
    	    catch ( Exception e )
    	    {
    	      scene = null;
    	      System.err.println( e );
    	    }

    	    if( scene == null )
    	      System.exit( 1 );

     
    	    tg1.addChild( scene.getSceneGroup( ) );
    	    viewing.addChild(tg1);
    	    
    	     
    	    //////////////Rotors///////////////////
    	    
    	    
    	    Appearance rotorAppearance= new Appearance();
    	    Material rotorMaterial = new Material( );
    	    rotorMaterial.setAmbientColor(0.0f, 0.0f, 0.0f);
    	    rotorMaterial.setSpecularColor(0.89f,0.89f,0.89f);
    	    rotorMaterial.setDiffuseColor(0.37f,0.37f,0.37f);
    	    rotorMaterial.setShininess(17.0f);
    	    rotorMaterial.setLightingEnable(true);
    	    rotorAppearance.setMaterial( rotorMaterial );
    	    
    	   // Create the blade primitive
    	    Box blade0 = new Box(0.25f, 0.002f, 0.004f, Box.GENERATE_NORMALS, rotorAppearance);
    	    Box blade1 = new Box(0.004f, 0.002f, 0.25f, Box.GENERATE_NORMALS, rotorAppearance);
    	    
    	    //transform to get blade0 in place
    	    Transform3D blade0Trans = new Transform3D();
    	    blade0Trans.setTranslation(new Vector3f(0.0f, -0.15f, -2.0f));
    	    TransformGroup tg2 = new TransformGroup(blade0Trans);

    	    
    	    //transform group for the pi
    	    TransformGroup tg3 = new TransformGroup();
    	    
    	    // Create Alpha
    	    Alpha rotationAlpha = new Alpha(-1, (1000));
    	    
    	    // Create the rotational interpolator to move the blades
    	    Transform3D yAxis = new Transform3D();
    	    RotationInterpolator pi = new RotationInterpolator(rotationAlpha, tg3, yAxis, 0.0f, (float) Math.PI*2.0f);
    	    
    	    pi.setSchedulingBounds(new BoundingSphere(new Point3d(),Double.POSITIVE_INFINITY));
    	 
    	    tg3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    	    tg3.addChild(pi);
    	    tg3.addChild(blade0);
    	    tg3.addChild(blade1);
    	    tg2.addChild(tg3);
    	    viewing.addChild(tg2);
    	    
    	   ///////////////////////////////////////  
    	 // the dynamic background
    	    Bounds infiniteBounds = new BoundingSphere(new Point3d(), Double.MAX_VALUE);
    	    Background background1 = new Background();
    	    background1.setApplicationBounds(infiniteBounds);
    	    
    	    
    	    Appearance appearanceB = new Appearance();
    	    TextureLoader loader = new TextureLoader("resources/skypattern.jpg",null);
    	    appearanceB.setTexture(loader.getTexture());
    	    
    	   Sphere sphere = new Sphere(20.0f, Sphere.GENERATE_NORMALS_INWARD |
					Sphere.GENERATE_TEXTURE_COORDS, 45, appearanceB);
    	   
    	   /////////////////////////////////////////////////
    	   //Fog
    	    Color3f skyblue = new Color3f(0.6f, 0.7f, 0.9f);
    	   LinearFog fog = new LinearFog (skyblue, 5.0f, 20.0f);
    	  //  ExponentialFog fog = new ExponentialFog (skyblue, 0.05f);
    	 //  Bounds fogBounds = new BoundingSphere(new Point3d(), Double.MAX_VALUE);
    	    fog.setInfluencingBounds(infiniteBounds);
    	    root.addChild(fog);
    	    
    	    root.addChild(sphere);
    	    ///////////////////////////////////////////////////////
    	    //Lights
    	    // Create a directional light with a bright white colour
    	    DirectionalLight light = new DirectionalLight(new Color3f(1.0f, 1.0f, 1.0f), 
    	        new Vector3f(-1.0f, -1.0f, -1.0f)); 
    	    light.setInfluencingBounds(new BoundingSphere(new Point3d(), 
    	        Double.MAX_VALUE));
    	    root.addChild(light);
    	    
    	    addBlinkingLight(root, -0.5f, -0.1f,  0.5f); 
    	    addBlinkingLight(root, 0.5f, -0.24f,  0.5f);
    	    addBlinkingLight(root, 0.5f,  -0.240f,  -0.5f);
    	    addBlinkingLight(root, -0.5f,  -0.240f,  -0.5f);
    	    
    	    vtg.addChild(viewing);
   
    	    addObject(root, -1 , -2, "resources/Maison_Exterieur_Final_cyril_Rizzon.obj");
    	    addObject(root, 1 , 1, "resources/plafond salle.obj");
    	    addObject(root, -1 , 1, "resources/B12.obj");
    	    addObject(root, 1 , -3, "resources/Windmill_100meter.obj");
    	    addObject(root, 0.5f , -3, "resources/Windmill_100meter.obj");
    	    addObject(root, 2 , -3, "resources/Windmill_100meter.obj");
    	  
    root.compile();

    return root;
  }
  public void addBlinkingLight(BranchGroup root, float x, float y, float z){
	  

	  	//set red appearance
	    Appearance appearance1 = new Appearance();
		Material material1 = new Material( );
		
		material1.setEmissiveColor(1.0f, 0.0f, 0.0f);
		material1.setDiffuseColor(1.0f, 0.0f, 0.0f);
		
		material1.setLightingEnable(true);
		appearance1.setMaterial( material1 );
		
		// red sphere
		Transform3D t1 = new Transform3D();
		t1.setTranslation(new Vector3f(x, y, z));
		TransformGroup tg1 = new TransformGroup(t1);
		Sphere redsphere = new Sphere(0.005f, Sphere.GENERATE_NORMALS, 50, appearance1);
		tg1.addChild(redsphere);
		
		//set black appearance
	    Appearance appearance2 = new Appearance();
		appearance2.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		ColoringAttributes blackColoring = new ColoringAttributes(new Color3f(0.0f,0.0f,0.0f), ColoringAttributes.SHADE_FLAT);
		appearance2.setColoringAttributes( blackColoring);
		
		// Black sphere
		TransformGroup tg2 = new TransformGroup(t1);
		Sphere blacksphere = new Sphere(0.005f, Sphere.GENERATE_NORMALS, 50, appearance2);
		tg2.addChild(blacksphere);
		
		// red point light 
		PointLight point1 = new PointLight(new Color3f(0.5f, 0.0f, 0.0f), 
										new Point3f(x, y, z), new Point3f(1.0f, -1.0f, 7.5f)); 
											//colour, position, attenuation
	    point1.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));
	    tg1.addChild(point1);
	    
	    
	    //create the Switch Node
	    Switch switchNode = new Switch();

	    //set the WRITE capability
	    switchNode.setCapability( Switch.ALLOW_SWITCH_WRITE );
		  //add children to switchNode here…
	    switchNode.addChild(tg1);
		switchNode.addChild(tg2);
	    
	    //create the Alpha for the Interpolator
		// cycle through the child nodes in the Switch Node
		Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 1000, 0, 0, 0, 0, 0);
	         
		//create the SwitchInterpolator and pass the Alpha and a Switch Node
		SwitchValueInterpolator switchInterpolator = new SwitchValueInterpolator( alpha, switchNode );
		switchInterpolator.setEnable(true);
		switchInterpolator.setSchedulingBounds(new BoundingSphere(new Point3d(),Double.POSITIVE_INFINITY) );
	  
		//add the Interpolator to a parent BranchGroup and set Scheduling Bounds
		BranchGroup bg = new BranchGroup();
		bg.addChild( switchInterpolator );
		bg.addChild(switchNode);
	    
		root.addChild(bg);
  }
  
 public void addObject(BranchGroup root, float x, float z, String file) {
	  
		Transform3D t = new Transform3D();
		t.setTranslation(new Vector3d(x, 0, z));
		
		t.setScale(0.2);
		TransformGroup tg = new TransformGroup(t);

		// load the object file
	    Scene scene = null;
	   // Shape3D shape = null;

	    // read in the geometry information from the data file
	    ObjectFile objFileloader = new ObjectFile( ObjectFile.RESIZE );

	    try
	    {
	      scene = objFileloader.load( file );
	    }
	    catch ( Exception e )
	    {
	      scene = null;
	      System.err.println( e );
	    }

	    if( scene == null )
	      System.exit( 1 );
 
	    tg.addChild( scene.getSceneGroup( ) );
	    root.addChild(tg);
	     
  }
 
  public Chopper()
  {
    super();
    canvas.addKeyListener(this);
  }

  public static void main(String args[])
  {
  //Create a new thread of Chopper() when the program is started
  	Thread thread = new Thread(new Chopper());
    thread.start(); 
  }

  public void keyReleased(KeyEvent ke)
  {
  }

  public void keyTyped(KeyEvent ke)
  {
  }


@Override
public void keyPressed(KeyEvent ke) {
	// Handle the up arrow (Up)
    if(ke.getKeyCode() == KeyEvent.VK_UP)
    	zspeed = zspeed + 0.015f; //increment speed by 0.1  

    // Handle the down arrow (Down)
    else if(ke.getKeyCode() == KeyEvent.VK_DOWN)
    	zspeed = zspeed - 0.015f; //decrement speed by 0.1
 // Handle the Left
    if(ke.getKeyCode() == KeyEvent.VK_LEFT)
    	xspeed = xspeed + 0.015f; //increment speed by 0.1  

    // Handle the Right
    else if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
    	xspeed = xspeed - 0.015f; //decrement speed by 0.1
    
    //as before
    // Handle the A
    else if(ke.getKeyCode() == KeyEvent.VK_A)
      angle += Math.toRadians(1);

    // handle the D
    else if(ke.getKeyCode() == KeyEvent.VK_D)
      angle -= Math.toRadians(1);
    
  //handle the W key
    if(ke.getKeyCode() == KeyEvent.VK_W)
    {
    	throttle = throttle + 0.015f; //increment speed by 0.1
    }
    //handle the S key
    if(ke.getKeyCode() == KeyEvent.VK_S)
    {
    	throttle = throttle - 0.015f; //decrement speed by 0.1
    }
   
}


@Override
public void run() {
	
	while(true){
		Transform3D transform = new Transform3D();
		
	    vtg.getTransform(transform);
	    Vector3f translation = new Vector3f();
	      
	    transform.get(translation);
	    
	    //when thread is run it uses speed variable which may have changed due to key pressed method
	    translation.z -= Math.cos(angle)*zspeed;
	    translation.x -= Math.sin(angle)*zspeed;
	    
	    translation.x -= Math.cos(angle)*xspeed;
	    translation.z -= Math.sin(angle)*xspeed;
	    
	    if(translation.y>-0.002)
	    translation.y += throttle;
	    else 
	    	translation.y -= throttle;
	    
	    
	    /////////////Position
	    float posx1, posz1, distance;  
	    
	    posx1 = translation.x;
	    posz1 = translation.z;
	    System.out.println("posx1,posz1"+ posx1+","+posz1);
	    distance = (float) Math.sqrt(((posx-posx1)*(posx-posx1))+((posz-posz1)*(posz-posz1)));
	    speed = distance*100;
	    speed1 = (float) (Math.round(speed*100.0)/100.0);
	    // System.out.println("posx1,posz1"+ posx1+","+posz1);
	    System.out.println("posx1,posz1"+ posx1+","+posz1);
	    System.out.println("speed"+ speed1);
	    

		
	    
	    //handle left/right turning on y axis
	    rotation.set(0,100,0,angle);
	
	    
	    transform.setTranslation(translation);
	    transform.setRotation(rotation);
	    vtg.setTransform(transform); //sets transform for transform group of viewing platform
	        
	    
	    try {Thread.sleep(100);} 
	    	catch (InterruptedException e) 
	    	{System.out.println("Thread Sleep error!");
	    	}
	    	posx = translation.x;
		    posz = translation.z;
		   
		}
	}

}
