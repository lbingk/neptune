package org.net;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-12 11:15
 */
public class FilterChain {
    interface Filter {
        public int invoke(Invoker invoker);
    }

    class Filter2 implements Filter {

        public int invoke(Invoker invoker) {
            System.out.println("Filter2");
            return invoker.invoke();
        }
    }

    class Filter1 implements Filter {

        public int invoke(Invoker invoker) {
            System.out.println("Filter1");
            invoker.invoke();
            return 0;
        }
    }

    interface Invoker {
        public int invoke();
    }


    public Invoker filterChain() {
        List<Filter> filters = Arrays.asList(new Filter1(), new Filter2());

        Invoker last = new Invoker() {
            public int invoke() {
                System.out.println("invoker");
                return 0;
            }
        };

        for (int i = filters.size() - 1; i >= 0; i--) {
            // 获取filter
            final Filter filter = filters.get(i);
            final Invoker next = last;

            // 更新last
            last = new Invoker() {
                public int invoke() {
                    return filter.invoke(next);
                }
            };
        }
        return last;
    }

    @Test
    public  void  filterChainTest(){
        Invoker invoker = filterChain();
        System.out.println(invoker);
    }
}
