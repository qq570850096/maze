import java.util.ArrayList;

public class randomQueue<E> {
  private ArrayList<E> queue;

  public randomQueue(){
    queue=new ArrayList<E>();
  }

  public void add(E e){
    queue.add(e);
  }

  public E remove(){

    if(queue.size()==0){
      throw new IllegalArgumentException("队列空！");
    }

    int randIndex=(int)(Math.random()*queue.size());

    E randElement = queue.get(randIndex);
    queue.set(randIndex, queue.get(queue.size()-1));
    queue.remove(queue.size()-1);

    return randElement;
  }

  public int size(){
    return queue.size();
  }

  public boolean empty(){
    return size()==0;
  }
}