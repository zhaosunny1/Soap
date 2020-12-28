package zhaoyang.soap;


import com.zyy.soap.annonations.WebParam;
import com.zyy.soap.annonations.WebService;

import io.reactivex.Observable;


@WebService(targetNamespace = "http://dao.ws.cbsw.cn/",
        targetEndPoint = "cxf/loginService")
public interface ILoginService {

    Observable<UserInfo> login(@WebParam(name = "username") String username,
                               @WebParam(name = "password") String password);

    String logout(@WebParam(name = "username") String username,
                  @WebParam(name = "shebei") String shebei);
}