package mo.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JudgeIdMap {
    //judgeId c->2 c++->7 java->8 python->10
    //Vue 0->java 1->c 2->c++ 3->python
    private static final int[] judgeIds = {8,2,5,10};
    private static final int[] results = {};
    public static Integer getJudgeId(Integer language){
        if(language>3||language<0)
            return 1;
        else
            return judgeIds[language];
    }
}
