package level;

public class CollisionLayer {
	
	private CollisionType[][] collisionTypes;

	public CollisionLayer(int width, int height){

		collisionTypes = new CollisionType[height][width];

	}
	
	public CollisionType getType(int x, int y){
		
		return collisionTypes[y][x];
		
	}
	
	public AABB getCollisionBox(int x, int y){
		
		AABB collisionBox = new AABB(x * 16, y * 16, 16, 16);
		return collisionBox;
		
	}
	
	public void set(int x, int y, CollisionType type){

		collisionTypes[y][x] = type;

	}
}