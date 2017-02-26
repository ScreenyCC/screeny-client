package screeny.protocol.listener;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class EventHandlerMethod {
    private Object listener;
    private Method method;

    public EventHandlerMethod( Object listener, Method method ) {
        this.listener = listener;
        this.method = method;
    }
}
