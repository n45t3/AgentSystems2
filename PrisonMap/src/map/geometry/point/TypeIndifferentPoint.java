package map.geometry.point;

public class TypeIndifferentPoint<T> {
	
	private int dim ;
	private T[] coords ;
	
	@SuppressWarnings("unchecked")
	public TypeIndifferentPoint(T...args){
		if(args==null)throw new IllegalArgumentException(this.getClass().getName()+": null arguments") ;
		if(args.length==0)throw new IllegalArgumentException(this.getClass().getName()+": dimension zero") ;
		this.dim=args.length ;
		this.coords=(T[]) new Object[args.length] ;
	}
	
	public T getArg(int idx,boolean one_based){
		if(one_based)--idx ;
		if(idx>=dim)throw new IllegalArgumentException(this.getClass().getName()+": index out of bounds") ;
		return this.coords[idx] ;
	}
	public T getArg(int idx){
		return getArg(idx,false) ;
	}
	
	public void setArg(int idx,T val,boolean one_based){
		if(one_based)--idx ;
		if(idx>=dim)throw new IllegalArgumentException(this.getClass().getName()+": index out of bounds") ;
		this.coords[idx]=val ;
	}
	public void setArg(int idx,T val){
		setArg(idx,val,false) ;
	}
}
