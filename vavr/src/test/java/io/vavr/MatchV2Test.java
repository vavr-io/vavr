package io.vavr;


import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 *
 * @author tanglt
 * @version jdk 8
 */
public class MatchV2Test {

    @Test
    public void of() {
        String a = null;
        MatchV2<String, String> matchV2A = stringMatch(a);
        String caseValue = matchV2A.get();
        assertThat(caseValue).isEqualTo("is null");

        a = "null";
        matchV2A = stringMatch(a);
        caseValue = matchV2A.get();
        assertThat(caseValue).isEqualTo("null");

        a = "";
        matchV2A = stringMatch(a);
        caseValue = matchV2A.get();
        assertThat(caseValue).isEqualTo("is empty");

        a = " ";
        matchV2A = stringMatch(a);
        caseValue = matchV2A.get();
        assertThat(caseValue).isEqualTo("is blank");

        a = "a";
        matchV2A = stringMatch(a);
        caseValue = matchV2A.get();
        assertThat(caseValue).isEqualTo("def");


        /**
         *   String a = "";
         *   Integer b = 0;
         *    if...
         *
         *    else if(Objects.equals(b,0)){
         *        caseValue = 0;
         *    }
         */

        Integer b = 0;
        MatchV2<Integer, String> matchV2B = matchV2A.flatMap((m, r) -> MatchV2.of(b, r));
        caseValue = matchV2B.caseValue(0, "0").get();
        assertThat(caseValue).isEqualTo("0");

        MatchV2<String, String> matchV2C = matchV2B.mapMatch(String::valueOf);
        caseValue = matchV2C.caseValue("0","String 0").get();
        assertThat(caseValue).isEqualTo("String 0");

        Long caseValueB = matchV2B.caseValue(0, "0").map(Long::valueOf).get();
        assertThat((long)caseValueB).isEqualTo(0l);

        a = "";
        matchV2A = stringMatch(a);

        Integer b2 = 0;
        matchV2B = matchV2A.flatMap((m, r) -> MatchV2.of(b2, r));
        caseValue = matchV2B.caseValue(0, "0").get();
        assertThat(caseValue).isEqualTo("is empty");


        /**
         *
         *    if...
         *    else {
         *      caseValue = "is def2";
         *    }
         */

        a = "def";
        matchV2A = stringMatch(a);
        caseValue = matchV2A.orElse("def2");
        assertThat(caseValue).isEqualTo("def2");

        a = "def";
        matchV2A = stringMatch(a);
        caseValue = matchV2A.orElseGet(()->"def2");
        assertThat(caseValue).isEqualTo("def2");

        assertThatThrownBy(()->stringMatch("def").orElseThrow(NoSuchElementException::new)).isInstanceOf(NoSuchElementException.class);

    }

    private MatchV2<String, String> stringMatch(String matchString){

        /**
         *    String caseValue = "def";
         *    if(Objects.isNull(matchString)){
         *      caseValue = "null";
         *    } else if(Objects.equals(matchString,"null")){
         *      caseValue = "is null";
         *    } else if(StringUtils.isBlank(matchString)){
         *      caseValue = "is empty";
         *    } else if(StringUtils.isBlank(matchString)){
         *      caseValue = ()->"is blank";
         *    }
         */

        return MatchV2.of(matchString, "def")
                .caseNull("is null")
                .caseValue("null", "null")
                .caseValue(String::isEmpty, "is empty")
                .caseValue(StringUtils::isBlank, ()->"is blank");
    }
}