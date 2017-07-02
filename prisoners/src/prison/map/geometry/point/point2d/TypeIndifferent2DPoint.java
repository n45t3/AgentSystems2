package prison.map.geometry.point.point2d;

import prison.map.geometry.point.TypeIndifferentPoint;

public class TypeIndifferent2DPoint<T> extends TypeIndifferentPoint<T> {
	
	@SuppressWarnings("unchecked")
	public TypeIndifferent2DPoint(T x,T y){
		super(x,y) ;
	}
	
	public T getX(){
		return this.getArg(0) ;
	}
	public T getY(){
		return this.getArg(1);
	}
	
	public void setX(T val){
		this.setArg(0, val);
	}
	public void setY(T val){
		this.setArg(1, val);
	}
}
