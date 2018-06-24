/*
  The MIT License (MIT)

  Copyright (c) 2017 Rebase.it ReBot <just@rebase.it>

  Permission is hereby granted, free of charge, to any person obtaining a copy of
  this software and associated documentation files (the "Software"), to deal in
  the Software without restriction, including without limitation the rights to
  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
  the Software, and to permit persons to whom the Software is furnished to do so,
  subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package it.rebase.rebot.api.spi;

import it.rebase.rebot.api.object.MessageUpdate;

import java.util.Optional;

public interface CommandProvider {

    /**
     * Loads the command.
     */
    void load();

    /**
     * Executes the command and returns its result
     *
     * @param key           command parameters
     * @param messageUpdate message update
     * @return the query result based on the key
     */
    Object execute(Optional<String> key, MessageUpdate messageUpdate);

    /**
     * @return the command name
     */
    String name();

    /**
     * @return the command's help
     */
    String help();

    /**
     * method to be used with the dump command.
     *
     * @return a brief description about the command
     */
    String description();

    /**
     * Acceptable commands are, i.e: /ping or /ping@botUserId
     * If the botuserId is incorrect, the command will not be recognized.
     *
     * @param fullCommand full command in the format: /command@botID uptime
     * @param botUserId   botid to be extracted from command
     * @return the command to be executed.
     */
    default String extractCommand(String fullCommand, String botUserId) {
        // command may have parameters.
        return fullCommand.split(" ")[0].replace("@" + botUserId, "");
    }

    /**
     * only commands redirected to this bot will be processed
     *
     * @param messageUpdate message containing the command
     * @param botUserId     bot id, this will be used to make sure that this bot can process the given command
     * @return true - processable command
     * or
     * false - non processable command
     */
    default boolean canProcessCommand(MessageUpdate messageUpdate, String botUserId) {
        return true ? messageUpdate.getMessage().getText().startsWith("/") &&
                (messageUpdate.getMessage().getText().contains("@" + botUserId) || !messageUpdate.getMessage().getText().contains("@")) &&
                extractCommand(messageUpdate.getMessage().getText(), botUserId).equals(name()) : false;
    }

}