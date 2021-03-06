package com.github.vbauer.avconv4java.core;

import com.github.vbauer.avconv4java.model.AVStreamType;
import com.github.vbauer.avconv4java.util.AVUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Basic class for options.
 *
 * @author Vladislav Bauer
 */

public class AVOptions {

    private final List<AVOptions> builders = new LinkedList<AVOptions>();
    private final List<String> arguments = new LinkedList<String>();


    public static AVOptions create() {
        return new AVOptions();
    }


    @Override
    public String toString() {
        return AVUtils.join(build());
    }


    public AVOptions flags(final Object... flags) {
        if (!AVUtils.isEmpty(flags)) {
            final List<String> newFlags = new LinkedList<String>();
            for (final Object flag : flags) {
                if (flag == null) {
                    return this;
                }
                newFlags.add(String.valueOf(flag));
            }
            arguments.addAll(newFlags);
        }
        return this;
    }

    public AVOptions builders(final AVOptions... builders) {
        if (!AVUtils.isEmpty(builders)) {
            for (final AVOptions builder : builders) {
                if (builder instanceof AVRootOptions) {
                    throw new IllegalArgumentException("It's impossible to add root options as child node");
                }
                this.builders.add(builder);
            }
        }
        return this;
    }

    public List<String> build() {
        final List<String> result = new LinkedList<String>();
        result.addAll(arguments);
        for (final AVOptions builder : builders) {
            result.addAll(builder.build());
        }
        return result;
    }


    protected final String kb(final Number value) {
        return value == null ? null : value + "k";
    }

    protected final String sec(final Double position) {
        return format("%.2f", position);
    }

    protected final Integer even(final Integer value) {
        return value == null ? null : value - value % 2;
    }

    protected final String format(final String filter, final Object... params) {
        return filter == null || AVUtils.hasNull(params) ? null : String.format(filter, params);
    }

    protected final String specifyStream(final String flag, final AVStreamType streamType) {
        return flag != null ? streamType != null ? flag + ":" + streamType.getName() : flag : null;
    }

}
