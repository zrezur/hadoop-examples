package org.dp.smrim;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Engine {


    public <INPUT, OUTPUT> List<OUTPUT> map(List<INPUT> input, Function<INPUT, OUTPUT> transformation) {
        List<OUTPUT> result = new ArrayList<OUTPUT>(input.size());
        for (INPUT entry : input) {
            result.add(transformation.apply(entry));
        }
        return result;
    }

    private static class FromPartImpl<INPUT> implements FromPart<INPUT>{

        private List<INPUT> input;

        public FromPartImpl(List<INPUT> input) {
            this.input = input;
        }

        public <KEY> GroupByPart<KEY, INPUT> groupBy(Function<INPUT, KEY> keyCreator) {
            return new GroupByPartImpl<KEY, INPUT>(input, keyCreator);
        }
    }

    private static class GroupByPartImpl<KEY, INPUT> implements GroupByPart<KEY, INPUT>{

        private List<INPUT> input;
        private Function<INPUT, KEY> keyCreator;

        public GroupByPartImpl(List<INPUT> input, Function<INPUT, KEY> keyCreator) {
            this.input = input;
            this.keyCreator = keyCreator;
        }

        public <ACCUMULATOR> Map<KEY, ACCUMULATOR> fold(Supplier<ACCUMULATOR> accumulatorSupplier, FoldOperation<ACCUMULATOR, INPUT> operation) {
            Map<KEY, ACCUMULATOR> result = new HashMap<KEY, ACCUMULATOR>();
            for (INPUT entry : input) {
                KEY key = keyCreator.apply(entry);
                ACCUMULATOR accumulator = result.get(key);
                if(accumulator == null){
                    accumulator = accumulatorSupplier.get();
                    result.put(key, accumulator);
                }

                operation.apply(accumulator, entry);
            }
            return result;
        }
    }

    public <INPUT> FromPart<INPUT> from(List<INPUT> input) {
        return new FromPartImpl<INPUT>(input);
    }

    public interface FromPart<INPUT>{
        <KEY> GroupByPart<KEY, INPUT> groupBy(Function<INPUT, KEY> keyCreator);
    }

    public interface GroupByPart<KEY, INPUT>{
        <ACCUMULATOR> Map<KEY, ACCUMULATOR> fold(Supplier<ACCUMULATOR> accumulatorSupplier, FoldOperation<ACCUMULATOR, INPUT> operation);
    }

    public interface FoldOperation<ACCUMULATOR, INPUT>{
        void apply(ACCUMULATOR accumulator, INPUT input);
    }
}
