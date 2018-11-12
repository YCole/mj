package cole.net;

/**
 * <pre>
 *     author: Coleman
 *     time  : 2017/1/22
 *     desc  :
 * </pre>
 */
public interface NetChangeObserver {

    void onConnect(NetworkUtils.NetworkType type);

    void onDisConnect();
}
