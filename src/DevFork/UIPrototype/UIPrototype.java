/* OBSERVER PATTERN -------------------------------------*/
// The Subject interface grants a class that implements it the ability to be observed
// The object implementing this interface uses the notify method to update
//the Observer that is interested in the event/change
interface Subject{
	public void addObserver(Observer observer);
	public void removeObserver(int index);
	public void notify();
	// Only notify a specific Observer
	public void notify(int index);
}

// The Observer interface allows an object to receive events/notifications 
//from a subject via the update method
interface Observer{
	public void update();
}


/* COMPOSITE PATTERN -----------------------------------*/
// The abstract Component class is the base-class for the composite pattern
// Provides default behaviors for child classes
// We can combine/mix the Observer pattern here to add callback ability
abstract class Component{
	public void operation();
	public void add(Component child);
	public void remove(Component child);
	public Component getChild(int index);
}

// The Leaf class is a component that will not have any children
// Buttons, Scrollbars, Checkboxes etc... any Element that is a child 
class Leaf extends Component implements Subject{
	// List of observers
	ArrayList<Observer> observers;
	
	// We have to implement the methods from the Subject interface
	public void addObserver(Observer observer){
		this.observers.add(observer);
	}
	
	public void removeObserver(int index){
		if(this.observers.size() > 0 && index < this.observers.size()){
			this.observers.remove(index);
		}
	}
	
	public void notify(){
		// We should probably add an overloaded method to call a specific observer
		if(this.observers.size() > 0){
			for(Observer o : observers){
				o.update();
			}
		}
	}
	
	public void notify(int index){
		if(this.observers.size() > 0 && index < this.observers.size()){
			this.observers.get(index).update();
		}
	}
	
	// Method extended from Component
	@Override
	public void operation(){
		// This operation method can be used to trigger the leaf object's event
		//i.e. Button click, scrollbar move, text update, etc...
		//This operation will notify 1 or more of this object's observers depending on implementation
	}
}

// The Composite class allows nested children, can also recursively act upon children also
// Windows, Panels, Listboxes, Tabs etc... basically any Element that has a child
class Composite extends Component implements Subject{
	// List of observers
	ArrayList<Observer> observers;
	// We have to implement the methods from the Subject interface
	public void addObserver(Observer observer){
		this.observers.add(observer);
	}
	
	public void removeObserver(int index){
		this.observers.remove(index);
	}
	
	public void notify(){
		// We should probably add an overloaded method to call a specific observer
		if(this.observers.size() > 0){
			for(Observer o : observers){
				o.update();
			}
		}
	}
	
	public void notify(int index){
		if(this.observers.size() > 0 && index < this.observers.size()){
			this.observers.get(index).update();
		}
	}
	
	// Method extended from Component
	@Override
	public void operation(){};
	
	@Override
	public void add(Component child){};
	
	@Override
	public void remove(Component child){};
	
	@Override
	public Component getChild(int index){};
}

/* PROTOTYPE DESIGN ---------------------------------- */
class GUIElement {
	// Text-related members
	private BitmapFont font;
	private BitmapText text;
	private String sText;
	
	// Scene-graph related members
	private Node node;
	private Geometry[] geometry;
	private Vector2f localTranslation;
	private Vector2f size;
	private Material mat;
	
	// State-related members
	private int status;
	
	// These class members help the GUI maintain relationships
	private GUIElement parent;
	private ArrayList<GUIElement> children;
	
	// Callback interface
	private EventListener listener;
	
	public GUIElement(BitmapFont font, String text, int x, int y, int sizeX, int sizeY, Material mat) {
		this.font = font;
		this.sText = text;
		this.localTranslation = new Vector2f( x, y);
		this.size = new Vector2f(sizeX/2, sizeY/2);
		this.mat = mat;
		this.status = 0;
	}	
	
	// Set this element's parent
	public void setParent(GUIElement element) {
		this.parent = element;
	}
	
	// Add a child to the list of elements
	public void addChild(GUIElement element) {
		if(children.exists(element) == false) {
			this.children.add(element);
		}
	}
	
	// This abstract method will be used to constrcut 
	//the Element and return the node in order to attach to the scene graph
	public abstract Node buildElement();
	
	// Member to setup event listener
	public void registerEventListener( EventListener listener) {
		this.listener = listener;
	}
	
	// This method provides a method to call the callback
	public void callEvent() {
		if(this.listener != null) {
			this.listener.onEvent();
		}
	}
	
	// This method provides a method to call the callback with a code
	public void callEventCode(int code) {
		if(this.listener != null) {
			this.listener.onEventCode(code);
		}
	}
}
// This is an interface that will be used for GUI-related callbacks in order to respond to clicks
interface EventListener{
	
	void onEvent();
	void onEventCode(int code);
}

// This is a class that implements the EventListener that would be created inside the GUI thread to interact
//This class might benefit from being encapsulated in a main/global GUI object or AppState
class ButtonEvent implements EventListener{
	@Override
	public void onEvent() {
		// Custom button event being handled here
	}
	
	@Override
	public void onEventCode(int code) {
		// If diferent buttons are using the same event-hook,
		//then we can use the code to tell the difference.
	}
}

// An AppState could encapsulate an entire UI screen and its event listeners
// The UIState could also host factory methods to add GUIElements to the screen
class UIState extends AbstractAppState {
	private ArrayList<GUIElement> guiElements;
	
	// The constructor can take the root node of a GUI tree as a parameter
	//in order to build the UI-graph
	public UIState(GUIElement root){
		
	}
	
	// The elements on this UIState could be built during the init method
	@Override
	public void initialize(AppStateManager aSMgr, Application app) {
		super.initialize(aSMgr, app);
		
	}
}

class GUIButton extends GUIElement {
	
	public GUIButton(BitmapFont font, String text, int x, int y, int sizeX, int sizeY, Material mat) {
		super(font, text, x, y, sizeX, sizeY, mat);
	}
	
	@Override
	public Node buildElement() {
		// Create the text component
		this.text = new BitmapText(font,false);
        this.text.setSize(font.getCharSet().getRenderedSize());
        this.text.setText(sText);
        this.text.setLocalTranslation(x, y, 1);
		
		// Create the visual component 
		this.geometry = new Geometry(new Quad(size.x, size.y));
		this.geometry.setMaterial(mat);
		this.node = new Node();
		this.node.attachChild(this.text);
		this.node.attachChild(this.geometry);
		
		// Return the node that the element's geometry is attached to
		return this.node;
	}
}


class GUIPanel extends GUIElement {
	
	public GUIPanel(BitmapFont font, String text, int x, int y, int sizeX, int sizeY, Material mat) {
		super(font, text, x, y, sizeX, sizeY, mat);
	}
	
	@Override
	public Node buildElement() {
		// Panels should have an optional text title
		if(sText != null || !sText.equals("")) {
			this.text = new BitmapText(font,false);
			this.text.setSize(font.getCharSet().getRenderedSize());
			this.text.setText(sText);
			this.text.setLocalTranslation(x, y, 1);
		}
		
		// Create the visual component 
		this.geometry = new Geometry(new Quad(size.x, size.y));
		this.geometry.setMaterial(mat);
		this.node = new Node();
		this.node.attachChild(this.text);
		this.node.attachChild(this.geometry);
		
		// Return the node that the element's geometry is attached to
		return this.node;
	}
}
