package server;

public interface Service {
    default public void init()  {};
    default public void start() {} ;
    default public void stop()   {} ;
    default public void pause()   {} ;
    default public void resume()   {} ;
    default public void destroy()   {} ;
    default public void setName(String s) {};
    default public String getName() { return null; };

}