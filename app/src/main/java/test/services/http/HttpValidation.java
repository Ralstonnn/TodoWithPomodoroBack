package test.services.http;

import test.router.Router;

public class HttpValidation {
    public static boolean validateRoute(String route, String equalsTo) {
        route = route.replace(Router.PREFIX, "");
        return route.equals(equalsTo) || route.equals(equalsTo + "/");
    }
}
