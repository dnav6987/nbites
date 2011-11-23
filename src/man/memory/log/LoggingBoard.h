/**
 *
 * @LoggingBoard - a class that will handle all of the logging objects for each
 * of the memory object
 * Each item could potentially have a different logger (a threaded one, a gzip one, etc.)
 *
 * Also each logger depends on a OutProvider (which provides a means to write to some output)
 *
 * @author Octavian Neamtu
 */

#pragma once

#include <map>
#include <string>

#include "include/io/OutProvider.h"
#include "ClassHelper.h"

#include "MObjectLogger.h"
#include "memory/MObject.h"
#include "memory/Memory.h"

namespace man {
namespace memory {
namespace log {

class LoggingBoard {

	ADD_SHARED_PTR(LoggingBoard)

protected:
	typedef common::io::OutProvider OutProvider;
	typedef std::pair<MObject_ID, MObjectLogger::ptr > ObjectIOPair;
	typedef std::map<MObject_ID, MObjectLogger::ptr > ObjectIOMap;

public:
	LoggingBoard(Memory::const_ptr memory);
	virtual ~LoggingBoard() {
	}

	void newOutputProvider(OutProvider::ptr outProvider, MObject_ID id);

	void startLogging();
	void stopLogging();
	bool isLogging() {
		return logging;
	}

protected:
	//returns a NULL pointer if such a logger doesn't exist
	MObjectLogger::const_ptr getLogger(MObject_ID id) const;
	//returns a NULL pointer if such a logger doesn't exist
	MObjectLogger::ptr getMutableLogger(MObject_ID id);

private:
	Memory::const_ptr memory;
	bool logging;
	ObjectIOMap objectIOMap;
};
}
}
}
