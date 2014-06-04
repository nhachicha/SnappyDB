# Copyright (c) 2011 The LevelDB Authors. All rights reserved.
# Use of this source code is governed by a BSD-style license that can be
# found in the LICENSE file. See the AUTHORS file for names of contributors.

# Definitions of build variable common between Android and non-Android builds.

LOCAL_PATH := $(call my-dir)

C_INCLUDES = $(LOCAL_PATH) $(LOCAL_PATH)/include

SOURCES = \
	./db/db_iter.cc \
	./db/write_batch.cc \
	./db/builder.cc \
	./db/log_reader.cc \
	./db/table_cache.cc \
	./db/c.cc \
	./db/dbformat.cc \
	./db/version_edit.cc \
	./db/log_writer.cc \
	./db/filename.cc \
	./db/memtable.cc \
	./db/version_set.cc \
	./db/db_impl.cc \
	./db/repair.cc \
	./table/block.cc \
	./table/merger.cc \
	./table/block_builder.cc \
	./table/format.cc \
	./table/table.cc \
	./table/two_level_iterator.cc \
	./table/filter_block.cc \
	./table/iterator.cc \
	./table/table_builder.cc \
	./util/arena.cc \
	./util/cache.cc \
	./util/comparator.cc \
	./util/env_posix.cc \
	./util/histogram.cc \
	./util/crc32c.cc \
	./util/logging.cc \
	./util/bloom.cc \
	./util/coding.cc \
	./util/filter_policy.cc \
	./util/options.cc \
	./util/env.cc \
	./util/hash.cc \
	./util/status.cc \
	./snappy/snappy-sinksource.cc \
	./snappy/snappy-stubs-internal.cc \
	./snappy/snappy.cc \
	