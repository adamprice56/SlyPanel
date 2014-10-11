package adam.price.slypanel;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 02/10/14.
 */
public class SSHFragment extends Fragment {

    public SSHFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View SSHView = inflater.inflate(R.layout.fragment_ssh, container, false);

        return (SSHView);
    }
}
